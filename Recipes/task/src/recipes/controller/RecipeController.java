package recipes.controller;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.dto.RecipeDto;
import recipes.model.entity.RecipeEntity;
import recipes.repository.RecipesRepository;
import recipes.service.RecipeService;

import javax.validation.Valid;

@RestController
@Slf4j
@ToString
public class RecipeController {

    private final RecipesRepository repository;
    private final RecipeService service;

    public RecipeController(RecipesRepository repository, RecipeService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/api/recipe/{id}")
    public RecipeDto getRecipe(@PathVariable long id) {
        if (repository.existsById(id)) {
            RecipeDto dto = new RecipeDto(repository.getById(id));
            log.info("Recipe successfully returned. {}", dto);
            return dto;
        } else {
            log.warn("Not found recipe by id:{}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @PostMapping("/api/recipe/new")
    public String addRecipe(@RequestBody @Valid RecipeDto dto) {
        long entityId = service.submitRecipe(dto);
        return String.format("""
                {
                    "id": %d
                }
                """, entityId);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id) {
        if (repository.existsById(id)) {
            RecipeEntity deletingEntity = repository.getById(id);
            repository.deleteById(id);
            log.info("Successfully deleted a line: {}", deletingEntity);
            log.info("Return code: {}", ResponseEntity.noContent().build());
            return ResponseEntity.noContent().build();
        } else {
            log.info("Return code: {}", ResponseEntity.notFound().build());
            return ResponseEntity.notFound().build();
        }
    }
}

