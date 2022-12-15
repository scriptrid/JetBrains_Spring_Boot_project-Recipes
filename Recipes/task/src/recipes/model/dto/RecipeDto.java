package recipes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import recipes.model.entity.DirectionEntity;
import recipes.model.entity.IngredientEntity;
import recipes.model.entity.RecipeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecipeDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotEmpty
    private String[] ingredients;
    @NotEmpty
    private String[] directions;

    public RecipeDto(RecipeEntity entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.ingredients = entity.getIngredients()
                .stream()
                .map(IngredientEntity::getIngredient)
                .toList()
                .toArray(String[]::new);
        this.directions = entity.getDirections()
                .stream()
                .map(DirectionEntity::getDirection)
                .toList()
                .toArray(String[]::new);
    }
}
