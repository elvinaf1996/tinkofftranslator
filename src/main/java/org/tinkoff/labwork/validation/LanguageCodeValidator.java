package org.tinkoff.labwork.validation;

import org.tinkoff.labwork.enums.LanguageCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LanguageCodeValidator implements ConstraintValidator<ValidLanguageCode, String> {

    @Override
    public void initialize(ValidLanguageCode constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            LanguageCode.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
