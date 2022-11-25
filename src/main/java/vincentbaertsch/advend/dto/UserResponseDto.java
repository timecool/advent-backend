package vincentbaertsch.advend.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private String username;
    private String email;
    private List<RoleDto> roles;
}
