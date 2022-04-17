package com.tothenew.sharda.Dto.Request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AddAddressDto {

    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotBlank(message = "Zip code cannot be blank")
    @Size(min = 6, max = 6, message = "It should be exact 6 digits")
    private String zipcode;

    @NotBlank(message = "Label cannot be blank")
    private String label;
}