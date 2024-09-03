package de.ait.os.validation.validators;

import de.ait.os.validation.constrains.DayOfWeek;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 7/11/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

public class DayOfWeekValidator implements ConstraintValidator<DayOfWeek, String>{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        for (java.time.DayOfWeek dayOfWeek : java.time.DayOfWeek.values()) {
            if (dayOfWeek.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
