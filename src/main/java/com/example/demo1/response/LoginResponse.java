package com.example.demo1.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class LoginResponse {

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("access_token")
    private String accessToken;
}
