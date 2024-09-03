package de.ait.os.services;

import de.ait.os.dto.NewUserDto;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.dto.UserDto;
import de.ait.os.exceptions.RestException;
import de.ait.os.mail.MailTemplateUtil;
import de.ait.os.mail.OnlineShopMailSender;
import de.ait.os.models.ConfirmationCode;
import de.ait.os.models.User;
import de.ait.os.repositories.ConfirmationCodesRepository;
import de.ait.os.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 6/30/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@Service
@RequiredArgsConstructor
@Slf4j//включили журналирование, чтобы посмотреть, какой поток используется для метода register
public class UsersService {

    private final UsersRepository usersRepository;

    private final ConfirmationCodesRepository confirmationCodesRepository;

    private final PasswordEncoder passwordEncoder;

    private final OnlineShopMailSender mailSender;

    private final MailTemplateUtil mailTemplateUtil;

    @Value("${base.url}")
    private String baseUrl;

    @Transactional //нужно, чтобы не появился аккаунт, если не сформировался код
    public UserDto register(NewUserDto newUser) {

        log.info("Current thread for user registration: "+ Thread.currentThread().getName());
        
//сначала проверяем, есть ли пользователь в базе
        checkIfExistsByEmail(newUser);

//потом создаем пользователя, сохраняем пользователя в базу
        User user = saveNewUser(newUser);
        
//потом генерируем ссылку подтверждения для пользователя
        String codeValue = UUID.randomUUID().toString();

//потом генерируем сам код, сохраняем код в базу
        saveConfirmCode(user, codeValue);

//потом составляем саму ссылку полностью
        String link = createLinkForConfirmation(codeValue);

//составляем шаблон для папки mails
        String html = mailTemplateUtil.createConfirmationMail(user.getFirstName(), user.getLastName(), link);

//отправляем письмо со ссылкой подтверждения пользователю
        mailSender.send(user.getEmail(), "Registration", html); // @Async

        return UserDto.from(user);
    }

    private String createLinkForConfirmation(String codeValue) {

        return baseUrl + "/confirm.html?id="+ codeValue; //теперь ссылка указывает на frontend
    }

    private void saveConfirmCode(User user,String codeValue) {
        ConfirmationCode code = ConfirmationCode.builder()
                .code(codeValue)
                .user(user)
                //(действителен в течение 1 минуты)
                .expiredDateTime(LocalDateTime.now().plusMinutes(1))
                .build();

        confirmationCodesRepository.save(code);
    }

    private User saveNewUser(NewUserDto newUser) {
        User user = User.builder()
                .email(newUser.getEmail())
                .hashPassword(passwordEncoder.encode(newUser.getPassword()))
                .role(User.Role.USER)
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .state(User.State.NOT_CONFIRMED)
                .build();

        usersRepository.save(user);
        return user;
    }

    private void checkIfExistsByEmail(NewUserDto newUser) {
        
        if (usersRepository.existsByEmail(newUser.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email <" + newUser.getEmail() + "> already exists");
        }
    }

    public UserDto getUserById(Long currentUserId) {
        return UserDto.from(usersRepository.findById(currentUserId).orElseThrow());
    }

    @Transactional
    public UserDto confirm(String confirmCode) {
        ConfirmationCode code = confirmationCodesRepository
                .findByCodeAndExpiredDateTimeAfter(confirmCode, LocalDateTime.now())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Code not found or is expired"));

        User user = usersRepository
                .findFirstByCodesContains(code)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User by code not found"));

        user.setState(User.State.CONFIRMED);

        usersRepository.save(user);

        return UserDto.from(user);
    }

    //сделали метод для отправки сообщения от пользователя на электронную почту 100polok2018@gmail.com
//    public void send(StandardResponseDto body) {
//
//        mailSender.send("gal-chik@mail.ru", "some", body.getMessage());
//    }
}
