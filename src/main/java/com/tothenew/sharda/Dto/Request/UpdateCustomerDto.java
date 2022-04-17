package com.tothenew.sharda.Dto.Request;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UpdateCustomerDto {

    @NotBlank(message = "Access Token cannot be blank")
    private String accessToken;

    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;
}