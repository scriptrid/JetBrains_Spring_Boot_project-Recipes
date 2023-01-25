package recipes.controller;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.dto.CreatedRecipeDto;
import recipes.model.dto.RecipeDto;
import recipes.model.dto.RecipeUpdateDto;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@ToString
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping("/api/recipe/{id}")
    public RecipeDto getRecipe(@PathVariable long id) {
        return service.getRecipe(id).orElseThrow(() -> {
            log.warn("Entity not found by id {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        });
    }

    @GetMapping("/api/recipe/search")
    public List<RecipeDto> searchRecipes(String category, String name) {
        return service.findAll(category, name);
    }

    @PostMapping("/api/recipe/new")
    public CreatedRecipeDto addRecipe(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid RecipeUpdateDto dto) {
        long entityId = service.submitRecipe(dto, userDetails);
        return new CreatedRecipeDto(entityId);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id, @RequestBody @Valid RecipeUpdateDto dto) {
        service.updateEntityById(userDetails, id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        service.deleteRecipe(userDetails, id);
        log.info("Successfully deleted an entity by id : {}", id);
        log.info("Return code: {}", HttpStatus.NO_CONTENT.value());
        return ResponseEntity.noContent().build();
    }
}

