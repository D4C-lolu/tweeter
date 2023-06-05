package D4C.tweeter.dto.post;

import D4C.tweeter.dto.reaction.TweetReactionDTO;
import D4C.tweeter.model.user.User;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for fetching a tweet
 */
public class TweetDTO {
    private Long id;

    private String body;

    private User author;

    private Set<TweetDTO> comments;

    private Set<TweetReactionDTO> reactions;

    private LocalDateTime datePublished;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<TweetDTO> getComments() {
        return comments;
    }

    public void setComments(Set<TweetDTO> comments) {
        this.comments = comments;
    }

    public Set<TweetReactionDTO> getReactions() {
        return reactions;
    }

    public void setReactions(Set<TweetReactionDTO> reactions) {
        this.reactions = reactions;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TweetDTO)) return false;
        TweetDTO tweetDTO = (TweetDTO) o;
        return Objects.equals(getId(), tweetDTO.getId()) && Objects.equals(getBody(), tweetDTO.getBody()) && Objects.equals(getAuthor(), tweetDTO.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getAuthor());
    }

    @Override
    public String toString() {
        return "TweetDTO{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", author=" + author +
                ", datePublished=" + datePublished +
                '}';
    }
}
