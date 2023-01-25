package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.exceptions.RecipeNotFoundException;
import recipes.exceptions.WrongUserException;
import recipes.model.dto.RecipeDto;
import recipes.model.dto.RecipeUpdateDto;
import recipes.model.entity.DirectionEntity;
import recipes.model.entity.IngredientEntity;
import recipes.model.entity.RecipeEntity;
import recipes.model.entity.UserEntity;
import recipes.repository.RecipesRepository;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecipeService {

    private final RecipesRepository repository;
    private final UserService userService;

    public RecipeService(RecipesRepository repository,
                         UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public long submitRecipe(RecipeUpdateDto dto, UserDetails userDetails) {
        RecipeEntity entity = toEntity(dto, userDetails);
        repository.save(entity);
        log.info("""
                Successfully added a new recipe! {}
                Id of recipe: {}
                Count of lines in database: {}
                """, entity, entity.getId(), repository.count());
        return entity.getId();
    }

    @Transactional
    public void updateEntityById(UserDetails userDetails, long id, RecipeUpdateDto update) {
        RecipeEntity entity = getRecipeAndCheckAccess(id, userDetails);
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
    }

    @Transactional
    public void deleteRecipe(UserDetails userDetails, long id) {
        RecipeEntity entity = getRecipeAndCheckAccess(id, userDetails);
        repository.delete(entity);
    }

    public Optional<RecipeDto> getRecipe(long id) {
        Optional<RecipeEntity> entity = repository.findById(id);
        return entity.map(this::toDto);
    }

    private RecipeEntity getRecipeAndCheckAccess(long id, UserDetails userDetails) {
        Optional<RecipeEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        RecipeEntity entity = optional.get();
        if (!userDetails.getUsername().equals(entity.getAuthor().getEmail())) {
            throw new WrongUserException();
        }
        return entity;
    }

    private RecipeDto toDto(RecipeEntity entity) {
        return RecipeDto.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .date(entity.getDate())
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

    private RecipeEntity toEntity(RecipeUpdateDto dto, UserDetails details) {
        UserEntity user = userService.getUser(details);
        RecipeEntity entity = new RecipeEntity();
        entity.setName(dto.getName());
        entity.setCategory(dto.getCategory());
        entity.setDate(ZonedDateTime.now());
        entity.setDescription(dto.getDescription());
        entity.setIngredients(dto.getIngredients().stream()
                .map(ingredient -> new IngredientEntity(entity, ingredient))
                .toList());
        entity.setDirections(dto.getDirections().stream()
                .map(direction -> new DirectionEntity(entity, direction))
                .toList());
        entity.setAuthor(user);
        return entity;
    }

    public List<RecipeDto> findAll(String category, String name) {
        if ((category != null && name != null) || (category == null && name == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Sort sort = Sort.by("date").descending();

        Specification<RecipeEntity> spec = Specification.where(null);

        if (category != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category.toLowerCase()));
        }

        if (name != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        return repository.findAll(spec, sort).stream().map(this::toDto).toList();
    }
}
