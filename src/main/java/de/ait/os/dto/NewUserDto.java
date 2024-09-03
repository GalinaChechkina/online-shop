package de.ait.os.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 6/30/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@Data
@Schema(name = "NewUser", description = "Registration data")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    @NotNull
    @Email
    @Schema(description = "User's email address", example = "user@mail.com")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")
    @Schema(description = "The user's password", example = "Qwerty007!")
    private String password;

    @NotNull
    @Schema(description = "The user's First Name", example = "Petr")
    private String firstName;

    @NotNull
    @Schema(description = "The user's Last Name", example = "Petrov")
    private String lastName;

}
