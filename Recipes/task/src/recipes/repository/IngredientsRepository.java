package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.entity.IngredientEntity;

public interface IngredientsRepository extends JpaRepository<IngredientEntity, Long> {
}
