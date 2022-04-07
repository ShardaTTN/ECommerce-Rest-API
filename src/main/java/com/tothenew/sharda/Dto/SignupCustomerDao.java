package com.tothenew.sharda.Dto;

import com.tothenew.sharda.CustomValidation.PasswordMatchesForCustomer;
import com.tothenew.sharda.CustomValidation.ValidPassword;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@PasswordMatchesForCustomer
public class SignupCustomerDao {
    private String firstName;
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @ValidPassword
    private String password;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;
}