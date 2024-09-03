package de.ait.os.controllers.api;

import de.ait.os.dto.NewUserDto;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.dto.UserDto;
import de.ait.os.security.details.AuthenticatedUser;
import de.ait.os.validation.dto.ValidationErrorsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * 7/11/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@Tags(
        @Tag(name = "Users")
)
@RequestMapping("/api/users")
public interface UsersApi {

    @Operation(summary = "User registration", description = "Available to everyone. By default, the role is USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "The user is registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorsDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "There is already a user with this email address",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponseDto.class))),
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserDto register(@RequestBody @Valid NewUserDto newUser);

    @GetMapping("/confirm/{confirm-code}")
    UserDto getConfirmation(@PathVariable("confirm-code") String confirmCode);

//    @PostMapping("/temp")
//    public void send(@RequestBody StandardResponseDto body);

    @GetMapping("/profile")
    //@Parameter(hidden = true) - не показываем в документации, берем текущего авторизованного пользователя
    UserDto getProfile(@Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user);

}
