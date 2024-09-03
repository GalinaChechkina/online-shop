package de.ait.os.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 7/24/2024
 * OnlineShop
 *
 * @author Chechkina (AIT TR)
 */

@Component
@RequiredArgsConstructor
@Slf4j //включили журналирование, чтобы посмотреть, какой поток используется для метода send
public class OnlineShopMailSender {

    // создаем объект, для отправки сообщений(прочитает настройки в yml-file)
    private final JavaMailSender javaMailSender;

    // создаем метод для отправки сообщения на конкретный email
    @Async //нужна асинхронная функция, чтобы сообщение пользователю приходило сразу,
    // а письмо на почту приходило само по себе
    public void send(String email, String subject, String text){

        log.info("Current thread for email sending: "+ Thread.currentThread().getName());
    // создаем сообщение
        MimeMessage message = javaMailSender.createMimeMessage();
    // указываем кодировку сообщения (делаем Spring-обертку, чтобы было удобнее)
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            // задаем данные для письма
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);//отправляем текст на html-страницу

        } catch (MessagingException e){
            throw new IllegalStateException(e);
        }

    // отправляем это сообщение на почту
        javaMailSender.send(message);
    }
}
