package com.milk.restfilelogger.dto;

import lombok.Data;

/**
 * @author Jack Milk
 */
@Data
public class RegisterRequestDTO {
    private String email;
    private String name;
    private String password;
}
