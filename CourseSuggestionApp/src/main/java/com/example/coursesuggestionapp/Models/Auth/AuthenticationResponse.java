package com.example.coursesuggestionapp.Models.Auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;


    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}