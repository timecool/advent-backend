package vincentbaertsch.advend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincentbaertsch.advend.dto.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}