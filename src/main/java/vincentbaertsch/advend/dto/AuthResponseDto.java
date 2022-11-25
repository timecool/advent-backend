package vincentbaertsch.advend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String error;

    private List<UserChallenge> challenges;

    public AuthResponseDto(String accessToken, List<UserChallenge> challenges, String error) {
        this.challenges = challenges;
        this.accessToken = accessToken;
        this.error = error;
    }
}
