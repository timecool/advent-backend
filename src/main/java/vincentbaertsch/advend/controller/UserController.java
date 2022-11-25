package vincentbaertsch.advend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vincentbaertsch.advend.dto.AuthResponseDto;
import vincentbaertsch.advend.dto.UserEntity;
import vincentbaertsch.advend.repository.UserRepository;
import vincentbaertsch.advend.security.JWTGenerator;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;

    private JWTGenerator jwtGenerator;

    @Autowired
    public UserController(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JWTGenerator jwtGenerator)
    {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping("auth")
    public ResponseEntity<AuthResponseDto> isLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtGenerator.generateToken(authentication);
        Optional<UserEntity> user = userRepository.findByUsername(authentication.getName());
        if(!user.isPresent()) {
            return new ResponseEntity<>(new AuthResponseDto(null, null,"Something went wrong"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new AuthResponseDto(token, user.get().getChallenges(),null), HttpStatus.OK);
    }
}
