package vincentbaertsch.advend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vincentbaertsch.advend.dto.*;
import vincentbaertsch.advend.repository.ChallengeRepository;
import vincentbaertsch.advend.repository.RoleRepository;
import vincentbaertsch.advend.security.JWTGenerator;
import vincentbaertsch.advend.repository.UserRepository;
import vincentbaertsch.advend.security.SecurityConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/noauth")
public class AuthController {

    private RoleRepository roleRepository;
    private ChallengeRepository challengeRepository;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(
            ChallengeRepository challengeRepository,
            RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JWTGenerator jwtGenerator)
    {
        this.challengeRepository = challengeRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping("challenges")
    public List<Challenge> getAllChallenges() throws ParseException {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2022-11-01");
        // Date end = new Date();
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2022-12-12");
        return challengeRepository.findByDayBetween(start,end);
    }


    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        try{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<UserEntity> user = userRepository.findByUsername(loginDto.getUsername());
        if(!user.isPresent()) {
            return new ResponseEntity<>(new AuthResponseDto(null,null,"Something went wrong"),HttpStatus.BAD_REQUEST);
        }
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token,user.get().getChallenges(),null),HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(new AuthResponseDto(null,null,"Something went wrong"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterDto registerDto) {
        if(!registerDto.getEmail().endsWith("@byte5.de")){
            return new ResponseEntity<>(new AuthResponseDto("",null,"Please use your work email"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>(new AuthResponseDto("",null,"Username is taken!"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>(new AuthResponseDto("",null,"E-mail is taken!"), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        RoleDto roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerDto.getUsername(),
                        registerDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token,null, null),HttpStatus.OK);
    }
}
