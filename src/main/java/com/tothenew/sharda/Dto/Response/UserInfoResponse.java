package com.tothenew.sharda.Dto.Response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String email;
    private List<String> roles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }
}