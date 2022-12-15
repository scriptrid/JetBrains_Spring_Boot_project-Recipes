package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import recipes.model.dto.RecipeDto;
import recipes.model.entity.RecipeEntity;
import recipes.repository.RecipesRepository;

import javax.transaction.Transactional;

@Service
@Slf4j
public class RecipeService {

    private final RecipesRepository repository;
    private final DirectionService dirService;
    private final IngredientService ingService;

    public RecipeService(RecipesRepository repository, DirectionService dirService, IngredientService ingService) {
        this.repository = repository;
        this.dirService = dirService;
        this.ingService = ingService;
    }

    @Transactional
    public long submitRecipe(RecipeDto dto) {
        RecipeEntity entity = new RecipeEntity(dto);
        repository.save(entity);
        dirService.submitDirections(entity);
        ingService.submitIngredients(entity);
        log.info("""
                Successfully added a new recipe! {}
                Id of recipe: {}
                Count of lines in database: {}
                """, entity, entity.getId(), repository.count());
        return entity.getId();
    }
}
