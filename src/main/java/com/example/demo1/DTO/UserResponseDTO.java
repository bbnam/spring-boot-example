package com.example.demo1.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private int status;
    private int code;
    private List<UserDTO> result;
}
