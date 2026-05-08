package models;

public class CourierCredentials {
    private String login;
    private String password;

    public CourierCredentials() {}

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }

    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "CourierCredentials{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}