package de.ait.os.security.config;

import de.ait.os.models.User;
import de.ait.os.security.details.AuthenticatedUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 7/11/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@TestConfiguration
@Profile("test")//запускается только с тестами
public class TestSecurityConfig {

    public static final String MOCK_USER = "gollum@gmail.com";

    public static final String MOCK_ADMIN = "admin";

    @Bean
    @Primary//эта аннотация нужна для разрешения конфликта- у нас два бина с UserDetails вместо одного
    //этот конфликт будет и в основном приложении, но тут поможет аннотация @Profile("test"),
    //которая запускает конфигурацию со вторым бином только с тестами
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager() {

            //сами собираем пользователя для тестирования
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.equals(MOCK_USER)) {
                    return new AuthenticatedUser(User.builder()
                            .id(2L)
                            .email(MOCK_USER)
                            .role(User.Role.USER)
                            .state(User.State.CONFIRMED)
                            .build());
                } else if (username.equals(MOCK_ADMIN)) {
                    return new AuthenticatedUser(User.builder()
                            .id(1L)
                            .email(MOCK_ADMIN)
                            .role(User.Role.ADMIN)
                            .state(User.State.CONFIRMED)
                            .build());
                } else throw new UsernameNotFoundException("User not found");
            }
        };
    }
}
