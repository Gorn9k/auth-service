package ODL.web.authservice.entity.user;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ODL.web.authservice.entity.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AttributeOverride(name = "id", column = @Column(name = "r_id"))
public class Role extends AbstractEntity {

    @Column(nullable = false, length = 50)
    String name;

    @Column(nullable = false, length = 50)
    String displayName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    List<User> userList;

    @Column(nullable = false)
    Boolean freeAccess = false;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Role))
            return false;
        if (!super.equals(o))
            return false;
        Role role = (Role) o;
        return name.equals(role.name) && displayName.equals(role.displayName);
    }
}
