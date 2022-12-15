package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.entity.RecipeEntity;

public interface RecipesRepository extends JpaRepository<RecipeEntity, Long> {
}
