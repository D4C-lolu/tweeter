package D4C.tweeter.model.user;

import D4C.tweeter.model.post.Tweet;
import D4C.tweeter.model.reaction.TweetReaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A class representing a User object
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "user_name",nullable = false)
    private String userName;

    @NotNull
    @Column(name = "phone_number",unique = true, nullable = false)
    @Pattern(regexp = "(\\+234|0)[0-9]{10}")
    private String phoneNumber;

    @NotNull
    @Column(name = "password",nullable = false)
    private String password;

    @NotNull
    @Convert(converter = RoleAttributeConverter.class)
    @Column(name = "role",nullable = false)
    private Role role;

    @NotNull
    @Column(name = "signup_date",nullable = false)
    private LocalDateTime signupDate;

    @NotNull
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @NotNull
    @Column(name="status")
    private boolean active = true;

    @Column(name = "tweets")
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Tweet> tweets = new HashSet<>();

    @ManyToOne
    private User parent;

    @OneToMany(mappedBy = "parent")
    private Set<User> following = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    private Set<User> followers = new HashSet<>();

    @Column(name="reactions")
    @OneToMany(mappedBy = "user")
    private Set<TweetReaction> reactions = new HashSet<>();

    public User() {

    }

    /**
     * Default constructor with id param
     * @param id - ID of user entry
     * @param userName - Username of user
     * @param password - password for user
     * @param phoneNumber - 13/11 digit user phone number
     * @param role - User Role-One of USER,MODERATOR OR ADMIN
     */
    public User(Long id,String userName,String password,String phoneNumber,Role role){
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.signupDate = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
        this.role = role;
    }

    /**
     * Constructor without id param
     * @param userName - Username of user
     * @param phoneNumber - 13/11 digit user phone number
     * @param password - password for user
     * @param role - User Role-One of USER,MODERATOR OR ADMIN
     */
    public User(String userName,String phoneNumber,String password, Role role){
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.signupDate = LocalDateTime.now();
        this.lastSeen = LocalDateTime.now();
        this.role = role;

    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }


    public Set<TweetReaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<TweetReaction> reactions) {
        this.reactions = reactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getUserName(), user.getUserName()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && Objects.equals(getPassword(), user.getPassword()) && getRole() == user.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getPhoneNumber(), getPassword(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }
}
