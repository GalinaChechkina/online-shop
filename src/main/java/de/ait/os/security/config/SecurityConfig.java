package de.ait.os.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static de.ait.os.security.config.SecurityExceptionHandlers.*;

/**
 * 7/5/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@Configuration
@EnableWebSecurity//включили собственную настройку безопасности
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    //опишем бин SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf(AbstractHttpConfigurer::disable);//отключаем безопасность SS
        httpSecurity.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        //httpSecurity.cors(request -> getCorsConfiguration());//включаем cors для других доменов
        httpSecurity.formLogin(form-> {
                    //включаем страницу со входом по адресу "/api/login"
                    form.loginProcessingUrl("/api/login");
                    //описали поведение при успешном входе
                    form.successHandler(LOGIN_SUCCESS_HANDLER);
                    //описали поведение при неправильных данных для входа
                    form.failureHandler(LOGIN_FAILURE_HANDLER);
                });
        httpSecurity.logout(logout->{
            //включаем страницу с выходом по адресу "/api/logout"
            logout.logoutUrl("/api/logout");
            //описали поведение при успешном выходе
            logout.logoutSuccessHandler(LOGOUT_SUCCESS_HANDLER);
        });

        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                    .requestMatchers("/api/users/confirm/**").permitAll()
                    .requestMatchers("/api/users/files/**").permitAll()
                    .requestMatchers("/api/**").authenticated();

        });
//настраиваем собственную обработку ошибок безопасности
        httpSecurity.exceptionHandling(exceptionHandling ->{
            exceptionHandling.defaultAuthenticationEntryPointFor(
                    ENTRY_POINT,
                    new AntPathRequestMatcher("/api/**")//указываем, что обработка б. работать на всех endpoint-ах, начинающихся с api
            );
            exceptionHandling.accessDeniedHandler(ACCESS_DENIED_HANDLER);
        });

        return httpSecurity.build(); //собираем цепочку безопасности
    }

    //покажем SS, что надо работать с UserDetailsServiceImpl и что мы используем хеширование паролей
    @Autowired
    public void bindUserDetailsServiceAndPasswordEncoder(UserDetailsService userDetailsServiceImpl,
                                                         PasswordEncoder passwordEncoder,
                                                         AuthenticationManagerBuilder builder) throws Exception{

        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);

    }

    // https://learn.javascript.ru/fetch-crossorigin
//    private CorsConfiguration getCorsConfiguration() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
//        corsConfiguration.setAllowedHeaders(List.of("*"));
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setExposedHeaders(List.of("*"));
//        return corsConfiguration;
//    }
}
