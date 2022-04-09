package com.tothenew.sharda.Dto.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    private String zipcode;

    @NotBlank(message = "Label cannot be blank")
    private String label;
}