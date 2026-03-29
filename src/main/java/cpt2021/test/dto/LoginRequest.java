package cpt2021.test.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;  // 可以是用户名或邮箱
    private String password;
}