package vincentbaertsch.advend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincentbaertsch.advend.dto.Challenge;
import vincentbaertsch.advend.repository.ChallengeRepository;
import vincentbaertsch.advend.dto.UserChallenge;
import vincentbaertsch.advend.dto.UserEntity;
import vincentbaertsch.advend.repository.UserRepository;
import vincentbaertsch.advend.util.FileUploadUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/challenges")
public class ChallengeController {

    private ChallengeRepository challengeRepository;
    private UserRepository userRepository;

    public ChallengeController(ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }


    @PostMapping(path = "user/{id}")
    public ResponseEntity<UserEntity> setUserChallenge(@PathVariable("id") long id, @RequestParam("file")MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Optional<Challenge> challengeOptional = challengeRepository.findById(id);
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(userName);
       if(challengeOptional.isPresent() && userEntityOptional.isPresent()){
           Challenge currentChallenge= challengeOptional.get();
           String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
           String uploadDir = "./challenges/"+currentChallenge.getId()+"/" + userName;

           FileUploadUtil.saveFile(uploadDir, fileName, file);

           UserEntity user = userEntityOptional.get();
           List<UserChallenge> challenge = user.getChallenges();

           UserChallenge newChallenge = new UserChallenge();
           newChallenge.setFilePath(fileName);
           newChallenge.setChallenge(currentChallenge);
           challenge.add(newChallenge);
           user.setChallenges(challenge);

           userRepository.save(user);
           user.setPassword("");
           return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
       }
        return new ResponseEntity<>(new UserEntity(), HttpStatus.BAD_REQUEST);
    }
}
