package recipes.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "USER_SEQUENCE", sequenceName = "USER_SEQUENCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_SEQUENCE_ID")
    private Long id;


    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<RecipeEntity> recipes = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "email = " + email + ")";
    }
}