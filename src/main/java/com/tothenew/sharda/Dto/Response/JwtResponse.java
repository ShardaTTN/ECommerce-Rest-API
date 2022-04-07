package com.tothenew.sharda.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
