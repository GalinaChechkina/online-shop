package de.ait.os.security.details;

import de.ait.os.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 7/3/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

public class AuthenticatedUser implements UserDetails {

    private final User user;

    public AuthenticatedUser(User user){
        this.user= user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //права пользователя
        // в нашем случае, права пользователя определяются его ролью в приложении (например, USER, ADMIN, MANAGER)
/*
        // нужно взять роль пользователя как обычную строку
        String role = user.getRole().toString();

        //Оборачиваем ее в объект Spring Security
        SimpleGrantedAuthority authority= new SimpleGrantedAuthority(role);

        //Spring Security предполагает, что у пользователя м.б. много прав (ролей), поэтому он просит коллекцию
        List<GrantedAuthority> authorities = new ArrayList<>(); // создаем список
        authorities.add(authority);//кладем туда нашу роль

        return authorities;//возвращаем, как результат*/
        //если роль только одна м.б. в проекте
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getHashPassword();
    }

    @Override
    public String getUsername() {

        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { // аккаунт не просрочен?

        return user.getState().equals(User.State.BANNED);
    }

    @Override
    public boolean isAccountNonLocked() { // аккаунт не заблокирован?

        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() { // данные пользователя не просрочены?

        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() { // аккаунт активен?

        return user.getState().equals(User.State.CONFIRMED);
    }

    public Long getId(){
        return this.user.getId(); }
}
