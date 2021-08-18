package fi.bytecraft.spock.sso.domain;

import java.util.Date;

public final class UserLoginInfoBuilder {
    private String email;
    private Integer loginCount;
    private Integer wrongPasswordCount = 0;
    private String lastLoginIp;
    private Date lastValidatedToken;
    private Integer tokenValidForRemainingHours;

    private UserLoginInfoBuilder() {
    }

    public static UserLoginInfoBuilder anUserLoginInfo() {
        return new UserLoginInfoBuilder();
    }

    public UserLoginInfoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserLoginInfoBuilder withLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
        return this;
    }

    public UserLoginInfoBuilder withWrongPasswordCount(Integer wrongPasswordCount) {
        this.wrongPasswordCount = wrongPasswordCount;
        return this;
    }

    public UserLoginInfoBuilder withLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    public UserLoginInfoBuilder withLastValidatedToken(Date lastValidatedToken) {
        this.lastValidatedToken = lastValidatedToken;
        return this;
    }

    public UserLoginInfoBuilder withTokenValidForRemainingHours(Integer tokenValidForRemainingHours) {
        this.tokenValidForRemainingHours = tokenValidForRemainingHours;
        return this;
    }

    public UserLoginInfo build() {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setEmail(email);
        userLoginInfo.setLoginCount(loginCount);
        userLoginInfo.setWrongPasswordCount(wrongPasswordCount);
        userLoginInfo.setLastLoginIp(lastLoginIp);
        userLoginInfo.setLastValidatedToken(lastValidatedToken);
        userLoginInfo.setTokenValidForRemainingHours(tokenValidForRemainingHours);
        return userLoginInfo;
    }
}
