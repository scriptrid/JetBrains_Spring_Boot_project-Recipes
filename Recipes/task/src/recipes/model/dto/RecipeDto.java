package recipes.model.dto;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record RecipeDto(
        String name,
        String category,
        ZonedDateTime dateTime,
        String description,
        List<String> ingredients,
        List<String> directions
) {
}
