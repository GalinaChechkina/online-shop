package de.ait.os.controllers;

import de.ait.os.controllers.api.UsersApi;
import de.ait.os.dto.NewUserDto;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.dto.UserDto;
import de.ait.os.security.details.AuthenticatedUser;
import de.ait.os.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 6/30/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UsersService usersService;

    public UserDto register( NewUserDto newUser){
        return usersService.register(newUser);
    }

    @Override
    public UserDto getConfirmation(String confirmCode) {

        return usersService.confirm(confirmCode);
    }

//    @Override
//    public void send(StandardResponseDto body) {
//        usersService.send(body);
//    }

    public UserDto getProfile(AuthenticatedUser user){

        Long currentUserId = user.getId();
        return usersService.getUserById(currentUserId);
    }
}
