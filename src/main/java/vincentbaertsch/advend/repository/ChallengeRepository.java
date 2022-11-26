package vincentbaertsch.advend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincentbaertsch.advend.dto.Challenge;

import java.util.Date;
import java.util.List;


public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByDayLessThanEqual(Date end);
    boolean existsByDay(Date day);
}
