package D4C.tweeter.dto.user;

import D4C.tweeter.model.user.Role;

import java.util.Objects;

/**
 * DTO for creating a User entry
 */
public class UserCreationDTO {

    private String userName;
    private String password;
    private String phoneNumber;
    private Role role;

    public UserCreationDTO(){

    }

    public UserCreationDTO(String userName, String password, String phoneNumber, Role role) {

        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCreationDTO)) return false;
        UserCreationDTO that = (UserCreationDTO) o;
        return Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && getRole() == that.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword(), getPhoneNumber(), getRole());
    }

    @Override
    public String toString() {
        return "UserCreationDTO{" +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }
}
