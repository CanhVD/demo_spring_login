package com.example.demo1.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshTokenResquest {

    @JsonProperty("refresh_token")
    private String refreshToken;
}
