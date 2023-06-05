package D4C.tweeter.dto.user;

import D4C.tweeter.model.user.Role;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for fetching/updating a User entry
 */
public class UserDTO {

    private Long id;
    private String userName;
    private Role role;
    private LocalDateTime signupDate;
    private LocalDateTime lastSeen;
    private boolean active ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return isActive() == userDTO.isActive() && Objects.equals(getId(), userDTO.getId()) && Objects.equals(getUserName(), userDTO.getUserName()) && getRole() == userDTO.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getRole(), isActive());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                ", signupDate=" + signupDate +
                ", lastSeen=" + lastSeen +
                ", active=" + active +
                '}';
    }
}
