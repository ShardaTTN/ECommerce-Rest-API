package com.tothenew.sharda.Dto.Request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateSellerDto {

    @NotBlank(message = "Access Token cannot be blank")
    private String accessToken;

    @NotBlank(message = "First Name cannot be empty")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number must be of 10 digits")
    @NotBlank(message = "Phone number cannot be empty")
    private final String companyContact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email should be unique and valid")
    @NotBlank(message = "Email cannot be empty")
    private final String email;

    @NotBlank(message = "Company name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Company name should be unique")
    private final String companyName;

    @Size(min = 15, max = 15)
    @Pattern(regexp = "\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}", message = "GST number should be according to Indian Govt. norms")
    @NotBlank(message = "GST number cannot be empty")
    private final String gstNumber;

}
