package com.example.demo1.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResquest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
