package D4C.tweeter.dto.post;

import D4C.tweeter.model.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO class for creating a Tweet
 */
public class TweetCreationDTO {

    private String body;

    private User author;

    public TweetCreationDTO(){}

    public TweetCreationDTO( String body, User author) {

        this.body = body;
        this.author = author;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TweetCreationDTO)) return false;
        TweetCreationDTO that = (TweetCreationDTO) o;
        return Objects.equals(getBody(), that.getBody()) && Objects.equals(getAuthor(), that.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash( getBody(), getAuthor());
    }

    @Override
    public String toString() {
        return "TweetCreationDTO{" +
                ", body='" + body + '\'' +
                ", author=" + author +
                '}';
    }
}
