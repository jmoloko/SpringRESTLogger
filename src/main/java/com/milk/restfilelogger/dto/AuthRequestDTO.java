package com.milk.restfilelogger.dto;

import lombok.Data;

/**
 * @author Jack Milk
 */
@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
