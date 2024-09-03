package de.ait.os.security.details;

import de.ait.os.models.User;
import de.ait.os.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 7/5/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
/*
        Optional<User> userOptional = usersRepository.findByEmail(email);

        if (userOptional.isPresent()){//если такой пользователь существует
            User user = userOptional.get();//получаем объект пользователя
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);//кладем пользователя в объект для Spring Security
            return authenticatedUser;//возвращаем результат работы метода- пользователя из бд, адаптированного для SS
        } else {
            throw new UsernameNotFoundException("User with email "+email+" not found");//возвращаем ошибку
        }
        return null;
 */
        User user = usersRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User with email "+email+" not found"));
        return new AuthenticatedUser(user);

    }
}
