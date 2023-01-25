package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.entity.UserEntity;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
}
