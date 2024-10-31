package com.example.demo1.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResquest {

    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;
}
