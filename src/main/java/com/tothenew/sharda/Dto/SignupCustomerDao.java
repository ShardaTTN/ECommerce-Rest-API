package com.tothenew.sharda.Dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignupCustomerDao {
    private String firstName;
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,15}$")
    @Size(min = 8, max = 16, message = "Password must be equal or greater than 8 characters but less than 16 characters")
    private String password;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 16, message = "Password should be same to Password")
    private String confirmPassword;
}