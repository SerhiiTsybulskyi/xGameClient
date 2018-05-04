package c4r0n0s.xgameclient.entities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AccountEntity {
    public String deviceId;
    public String login;
    public String password;
    public Integer refreshTime;
    public Boolean autoLogin;

    public AccountEntity() {}

    public AccountEntity(String deviceId, String login, String password, Integer refreshTime,
                         Boolean autoLogin) {
        this.deviceId = deviceId;
        this.login = login;
        this.password = password;
        this.refreshTime = refreshTime;
        this.autoLogin = autoLogin;
    }

    @Override
    public String toString() {
        return "deviceId=" + deviceId + '\n' +
                "login=" + login + '\n' +
                "password=" + password + '\n' +
                "refreshTime=" + (refreshTime != null ? refreshTime/60000 : "") + " m \n" +
                "autoLogin=" + autoLogin;
    }
}
