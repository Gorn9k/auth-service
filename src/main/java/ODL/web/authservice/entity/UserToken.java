package ODL.web.authservice.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import ODL.web.authservice.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @Column(name = "token")
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "u_id")
    private User user;

    @Column(name = "created", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created;

    @Column(name = "expired", nullable = false)
    private LocalDateTime expired;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserToken))
            return false;
        UserToken userToken = (UserToken) o;
        return token.equals(userToken.token) && user.equals(userToken.user) && created.equals(userToken.created)
                && expired.equals(userToken.expired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user, created, expired);
    }
}
