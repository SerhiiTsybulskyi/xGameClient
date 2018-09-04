package c4r0n0s.xgameclient.entities;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AccountEntity {
    public String deviceId;
    public String login;
    public String password;
    public Integer refreshTime;
    public Integer uniNumber;
    public Boolean autoLogin;

    public AccountEntity() {}

    public AccountEntity(String deviceId, String login, String password, Integer refreshTime,
                         Integer uniNumber, Boolean autoLogin) {
        this.deviceId = deviceId;
        this.login = login;
        this.password = password;
        this.refreshTime = refreshTime;
        this.uniNumber = uniNumber;
        this.autoLogin = autoLogin;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "deviceId='" + deviceId + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", refreshTime=" + refreshTime +
                ", uniNumber=" + uniNumber +
                ", autoLogin=" + autoLogin +
                '}';
    }
}
