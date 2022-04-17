package com.tothenew.sharda.Dto.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;

    public JwtResponse(String accessToken, String refreshToken) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
    }
}
