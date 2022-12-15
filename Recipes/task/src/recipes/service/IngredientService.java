package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import recipes.model.entity.RecipeEntity;
import recipes.repository.IngredientsRepository;

@Service
@Slf4j
public class IngredientService {

    private final IngredientsRepository repository;

    public IngredientService(IngredientsRepository repository) {
        this.repository = repository;
    }

    public void submitIngredients(RecipeEntity entity) {
        entity.getIngredients().forEach(ingredient -> ingredient.setRecipe(entity));
        entity.getIngredients().forEach(ingredient -> {
            repository.save(ingredient);
            log.info("Ingredient successfully added: {}", ingredient);
        });
    }

}
