package fi.bytecraft.spock.sso;

import fi.bytecraft.spock.sso.domain.LoginAction;
import fi.bytecraft.spock.sso.domain.LoginException;
import fi.bytecraft.spock.sso.domain.LogoutException;
import fi.bytecraft.spock.sso.domain.SsoLogin;
import fi.bytecraft.spock.sso.domain.UserLoginInfo;
import fi.bytecraft.spock.sso.domain.UserLoginInfoBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
public class SingleSignOnService {

    private ExternalSSOClient ssoClient;
    private UserLoginInfoRepository loginInfoRepository;

    public SingleSignOnService(ExternalSSOClient ssoClient, UserLoginInfoRepository loginInfoRepository) {
        this.ssoClient = ssoClient;
        this.loginInfoRepository = loginInfoRepository;
    }

    public LoginAction login(String userEmail, String password, String ip) throws LoginException {
        if(isLoggedIn(userEmail) && !needsToRequestNewToken(userEmail))
            return createLoginActionForAlreadyLoggedInUser();

        return createLoginActionForFreshUser(userEmail, password, ip);
    }

    private boolean isLoggedIn(String userEmail) {
        Optional<UserLoginInfo> loginInfoOptional = loginInfoRepository.findByEmail(userEmail);
        return loginInfoOptional
                .map(UserLoginInfo::isLoggedIn)
                .orElse(ssoClient.isLoggedIn(userEmail));
    }

    private boolean needsToRequestNewToken(String userEmail) {
        Optional<UserLoginInfo> loginInfoOptional = loginInfoRepository.findByEmail(userEmail);
        return loginInfoOptional
                .map(UserLoginInfo::needsToRequestNewToken)
                .orElse(true);
    }

    private LoginAction createLoginActionForAlreadyLoggedInUser() {
        LoginAction loginAction = new LoginAction();
        loginAction.setLoggedIn(true);

        return loginAction;
    }

    private LoginAction createLoginActionForFreshUser(String userEmail, String password, String ip) throws LoginException {
        SsoLogin ssoLoginInfo;
        try {
            ssoLoginInfo = ssoClient.login(userEmail, password);
        } catch (Exception ex) {
            throw new LoginException("Login is disabled at the moment");
        }

        if (ssoLoginInfo.isLoginSucceeded()) {
            UserLoginInfo loginInfo = getUserLoginInfoByEmail(userEmail);
            updateCurrentLoginInfoForUserLogin(ssoLoginInfo, loginInfo, ip);
            return createLoginActionForSuccessfulLogin(ssoLoginInfo);
        }

        UserLoginInfo loginInfo = getUserLoginInfoByEmail(userEmail);
        updateFailedLoginInfoForUserLogin(loginInfo);
        return createLoginActionForFailedLogin();
    }

    private UserLoginInfo getUserLoginInfoByEmail(String userEmail) {
        Optional<UserLoginInfo> loginInfoOptional = loginInfoRepository.findByEmail(userEmail);
        return loginInfoOptional.orElse(
                UserLoginInfoBuilder.anUserLoginInfo()
                        .withLoginCount(0)
                        .withEmail(userEmail)
                        .build()
        );
    }

    private void updateCurrentLoginInfoForUserLogin(SsoLogin ssoLoginInfo, UserLoginInfo loginInfo, String ip) {
        loginInfo.setLastLoginIp(ip);
        loginInfo.setLastValidatedToken(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        loginInfo.setWrongPasswordCount(0);
        loginInfo.setTokenValidForRemainingHours(ssoLoginInfo.getTokenValidForHours());
        loginInfo.setLoginCount(loginInfo.getLoginCount()+1);
        loginInfo.setLoggedIn(true);
        loginInfoRepository.save(loginInfo);
    }

    private LoginAction createLoginActionForSuccessfulLogin(SsoLogin ssoLoginInfo) {
        LoginAction loginAction = new LoginAction();
        loginAction.setLoggedIn(true);
        loginAction.setToken(ssoLoginInfo.getToken());

        return loginAction;
    }

    private void updateFailedLoginInfoForUserLogin(UserLoginInfo loginInfo) {
        loginInfo.setWrongPasswordCount(loginInfo.getWrongPasswordCount() + 1);
        loginInfo.setLoggedIn(false);
        loginInfoRepository.save(loginInfo);
    }

    private LoginAction createLoginActionForFailedLogin() {
        LoginAction loginAction = new LoginAction();
        loginAction.setLoggedIn(false);

        return loginAction;
    }

    public boolean logout(String userEmail) throws LogoutException {
        try {
            ssoClient.logout(userEmail);
        } catch (Exception e) {
            throw new LogoutException("User logout failed!");
        }

        Optional<UserLoginInfo> loginInfoOptional = loginInfoRepository.findByEmail(userEmail);
        UserLoginInfo loginInfo = loginInfoOptional.orElseThrow(() -> new RuntimeException("Can't logout without a logged in user"));
        loginInfo.setLoggedIn(false);
        loginInfoRepository.save(loginInfo);

        return true;
    }

}
