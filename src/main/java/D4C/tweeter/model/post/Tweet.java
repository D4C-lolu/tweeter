package D4C.tweeter.model.post;

import D4C.tweeter.model.reaction.TweetReaction;
import D4C.tweeter.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A class representing a short post created by a user
 * Each tweet has a 140-character text message
 */
@Entity
@Table(name="post")
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name="body",length = 140)
    private String body;

    @NotNull
    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;

    @ManyToOne(cascade={CascadeType.ALL})
    private Tweet comment;

    @OneToMany(mappedBy="comment")
    private Set<Tweet> comments = new HashSet<>();


    @Column(name="user_reactions")
    @OneToMany(mappedBy = "post")
    private Set<TweetReaction> reactions = new HashSet<>();

    @NotNull
    @Column(name="date_published")
    private LocalDateTime datePublished;

    public Tweet(){

    }

    /**
     *
     * @param id - Tweet id
     * @param body - Tweet message
     * @param author - User tweet belongs to
     */
    public Tweet(Long id, String body, User author) {
        this.id = id;
        this.body = body;
        this.author = author;
        this.datePublished = LocalDateTime.now();
    }

    /**
     * @param body - Tweet message
     * @param author - User tweet belongs to
     */
    public Tweet(String body, User author) {
        this.body = body;
        this.author = author;
        this.datePublished = LocalDateTime.now();

    }

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public User getAuthor() {
        return author;
    }


    public Set<Tweet> getComments() {
        return comments;
    }

    public void setComments(Set<Tweet> comments) {
        this.comments = comments;
    }

    public Set<TweetReaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<TweetReaction> reactions) {
        this.reactions = reactions;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(getId(), tweet.getId()) && Objects.equals(getBody(), tweet.getBody()) && Objects.equals(getAuthor(), tweet.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getAuthor());
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", body='" + body +
                ", author=" + author +
                ", datePublished=" + datePublished +
                '}';
    }
}
