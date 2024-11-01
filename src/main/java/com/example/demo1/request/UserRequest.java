package com.example.demo1.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("create_by")
    private String createBy;

    @JsonProperty("update_by")
    private String updateBy;
}
