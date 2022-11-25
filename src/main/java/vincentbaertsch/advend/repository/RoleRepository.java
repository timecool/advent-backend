package vincentbaertsch.advend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincentbaertsch.advend.dto.RoleDto;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleDto, Integer> {
    Optional<RoleDto> findByName(String name);
}