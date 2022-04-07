package com.tothenew.sharda.CustomValidation;

import com.tothenew.sharda.Dto.SignupSellerDao;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidatorForSeller implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final SignupSellerDao user = (SignupSellerDao) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}