package fi.bytecraft.spock.sso.domain;

import java.io.Serializable;

public class SsoLogin implements Serializable {

    private boolean loginSucceeded;

    private Integer currentPasswordWrongCount;

    private Integer tokenValidForHours;

    private String token;

    public boolean isLoginSucceeded() {
        return loginSucceeded;
    }

    public void setLoginSucceeded(boolean loginSucceeded) {
        this.loginSucceeded = loginSucceeded;
    }

    public Integer getCurrentPasswordWrongCount() {
        return currentPasswordWrongCount;
    }

    public void setCurrentPasswordWrongCount(Integer currentPasswordWrongCount) {
        this.currentPasswordWrongCount = currentPasswordWrongCount;
    }

    public Integer getTokenValidForHours() {
        return tokenValidForHours;
    }

    public void setTokenValidForHours(Integer tokenValidForHours) {
        this.tokenValidForHours = tokenValidForHours;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
