package recipes.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;
import recipes.model.dto.RecipeDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
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
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<IngredientEntity> ingredients;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<DirectionEntity> directions;

    public RecipeEntity(RecipeDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.ingredients = Arrays.stream(dto.getIngredients())
                .map(IngredientEntity::new)
                .toList();
        this.directions = Arrays.stream(dto.getDirections())
                .map(DirectionEntity::new)
                .toList();
    }
}
