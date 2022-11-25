package vincentbaertsch.advend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vincentbaertsch.advend.dto.*;
import vincentbaertsch.advend.repository.ChallengeRepository;
import vincentbaertsch.advend.repository.UserRepository;
import vincentbaertsch.advend.security.SecurityConstants;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    UserRepository userRepository;
    ChallengeRepository challengeRepository;

    public AdminController(UserRepository userRepository, ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("users")
    public List<UserEntity> getAllChallenges() {
        List<UserEntity> result = userRepository.findAll();
        result.forEach(user -> user.setPassword(null));
        return result;
    }

    @PostMapping("challenge")
    public ResponseEntity<String> createChallenge(@RequestBody Challenge challenge) {
        if (challengeRepository.existsByDay(challenge.getDay())) {
            return new ResponseEntity<>("Date is taken!", HttpStatus.BAD_REQUEST);
        }
        challengeRepository.save(challenge);

        return new ResponseEntity<>("Challenge create!", HttpStatus.OK);
    }
}
