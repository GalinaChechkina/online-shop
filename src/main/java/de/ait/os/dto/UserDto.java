package de.ait.os.dto;

import de.ait.os.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 6/30/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User", description = "User Data")
public class UserDto {

    @Schema(description = "user ID", example = "1")
    private Long id;

    @Schema(description = "username", example = "Marsel")
    private String firstName;

    @Schema(description = "last name of the user", example = "Sidikov")
    private String lastName;

    @Schema(description = "User's email address", example = "user@mail.com")
    private String email;

    @Schema(description = "User Role", example = "USER")
    private String role;

    public static UserDto from(User user){

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();

    }

    public static List<UserDto> from(Collection<User> users) {
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
