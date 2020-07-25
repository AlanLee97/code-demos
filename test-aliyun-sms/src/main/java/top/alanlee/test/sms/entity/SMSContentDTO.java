package top.alanlee.test.sms.entity;

import java.io.Serializable;

public class SMSContentDTO implements Serializable {
    private String username;
    private String appointmentTitle;
    private String hour;

    public SMSContentDTO(String username, String appointmentTitle, String hour) {
        this.username = username;
        this.appointmentTitle = appointmentTitle;
        this.hour = hour;
    }

    public SMSContentDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "SMSContentDTO{" +
                "username='" + username + '\'' +
                ", appointmentTitle='" + appointmentTitle + '\'' +
                ", hour='" + hour + '\'' +
                '}';
    }
}
