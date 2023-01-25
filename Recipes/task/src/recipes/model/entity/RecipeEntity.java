package recipes.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Validated
@Table(name = "recipes")
@Setter
@Getter
@NoArgsConstructor
public class RecipeEntity {
    @Id
    @SequenceGenerator(name = "RECIPE_SEQUENCE", sequenceName = "RECIPE_SEQUENCE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "RECIPE_SEQUENCE_ID")
    private long id;

    @NotBlank
    @Column(name = "category")
    private String category;
    @Column(name = "date")
    private ZonedDateTime date;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientEntity> ingredients;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DirectionEntity> directions;


    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "category = " + category + ", " +
                "date = " + date + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "author = " + author + ")";
    }
}
