package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import recipes.model.dto.RecipeDto;
import recipes.model.dto.RecipeUpdateDto;
import recipes.model.entity.DirectionEntity;
import recipes.model.entity.IngredientEntity;
import recipes.model.entity.RecipeEntity;
import recipes.repository.RecipesRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Slf4j
public class RecipeService {

    private final RecipesRepository repository;

    public RecipeService(RecipesRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public long submitRecipe(RecipeUpdateDto dto) {
        RecipeEntity entity = new RecipeEntity(dto);
        repository.save(entity);
        log.info("""
                Successfully added a new recipe! {}
                Id of recipe: {}
                Count of lines in database: {}
                """, entity, entity.getId(), repository.count());
        return entity.getId();
    }

    @Transactional
    public boolean updateEntityById(long id, RecipeUpdateDto update) {
        Optional<RecipeEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }
        RecipeEntity entity = optional.get();
        entity.setName(update.getName());
        entity.setDescription(update.getDescription());
        entity.setCategory(update.getCategory());
        entity.getDirections().clear();
        entity.getDirections().addAll(update.getDirections()
                .stream()
                .map(direction -> new DirectionEntity(entity, direction))
                .toList());
        entity.getIngredients().clear();
        entity.getIngredients().addAll(update.getIngredients()
                .stream()
                .map(ingredient -> new IngredientEntity(entity, ingredient))
                .toList());
        entity.setDate(ZonedDateTime.now());

        log.info("A recipe by id {} was successfully updated: {}", id, entity);
        return true;
    }

    @Transactional
    public boolean deleteRecipe(long id) {
        boolean success = repository.existsById(id);
        repository.deleteById(id);
        return success;
    }

    public Optional<RecipeDto> getRecipe(long id) {
        Optional<RecipeEntity> entity = repository.findById(id);
        return entity.map(this::toDto);
    }

    private RecipeDto toDto(RecipeEntity entity) {
        return RecipeDto.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .dateTime(entity.getDate())
                .ingredients(entity.getIngredients()
                        .stream()
                        .map(IngredientEntity::getIngredient)
                        .toList())
                .directions(entity.getDirections()
                        .stream()
                        .map(DirectionEntity::getDirection)
                        .toList())
                .build();

    }
}
