package recipes.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import recipes.model.dto.RecipeUpdateDto;

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
@ToString
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
    @ToString.Exclude
    private List<IngredientEntity> ingredients;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DirectionEntity> directions;

    public RecipeEntity(RecipeUpdateDto dto) {
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.date = ZonedDateTime.now();
        this.description = dto.getDescription();
        this.ingredients = dto.getIngredients().stream()
                .map(ingredient -> new IngredientEntity(this, ingredient))
                .toList();
        this.directions = dto.getIngredients().stream()
                .map(direction -> new DirectionEntity(this, direction))
                .toList();
    }
}
