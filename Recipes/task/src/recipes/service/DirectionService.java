package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import recipes.model.entity.RecipeEntity;
import recipes.repository.DirectionsRepository;

@Service
@Slf4j
public class DirectionService {

    private final DirectionsRepository repository;

    public DirectionService(DirectionsRepository repository) {
        this.repository = repository;
    }

    public void submitDirections(RecipeEntity entity) {
        entity.getDirections().forEach(direction -> direction.setRecipe(entity));
        entity.getDirections().forEach(direction -> {
            repository.save(direction);
            log.info("Direction successfully added: {}", direction);
        });
    }
}
