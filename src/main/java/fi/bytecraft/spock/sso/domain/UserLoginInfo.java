package fi.bytecraft.spock.sso.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Entity
public class UserLoginInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    private String email;

    @Column
    private Integer loginCount;

    @Column
    private Integer wrongPasswordCount;

    @Column
    private String lastLoginIp;

    @Column
    private Date lastValidatedToken;

    @Column
    private Integer tokenValidForRemainingHours;

    @Column
    private Boolean isLoggedIn;

    public boolean needsToRequestNewToken() {
        return Date.from(
                LocalDateTime.now().minusHours(this.getTokenValidForRemainingHours()).toInstant(ZoneOffset.UTC)
        ).after(this.getLastValidatedToken());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getWrongPasswordCount() {
        return wrongPasswordCount;
    }

    public void setWrongPasswordCount(Integer wrongPasswordCount) {
        this.wrongPasswordCount = wrongPasswordCount;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastValidatedToken() {
        return lastValidatedToken;
    }

    public void setLastValidatedToken(Date lastValidatedToken) {
        this.lastValidatedToken = lastValidatedToken;
    }

    public Integer getTokenValidForRemainingHours() {
        return tokenValidForRemainingHours;
    }

    public void setTokenValidForRemainingHours(Integer tokenValidForRemainingHours) {
        this.tokenValidForRemainingHours = tokenValidForRemainingHours;
    }

    public Boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
