package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.entity.DirectionEntity;

public interface DirectionsRepository extends JpaRepository<DirectionEntity, Long> {
}
