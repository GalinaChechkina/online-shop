package de.ait.os.validation.constrains;

import de.ait.os.validation.validators.DayOfWeekValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //аннотацию м. повесить только на поля
@Retention(RetentionPolicy.RUNTIME)//аннотация не исчезнет после компиляции и запуска
@Constraint(validatedBy = DayOfWeekValidator.class)
public @interface DayOfWeek {

    String message() default "must be day of week"; // сообщение, которое получит пользователь при ошибке

    Class<?>[] groups() default {};//для чего-то нужно

    Class<? extends Payload>[] payload() default {};//для чего-то нужно
}
