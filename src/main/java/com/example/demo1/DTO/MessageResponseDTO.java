package com.example.demo1.DTO;

import com.example.demo1.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseDTO {
    private int status;
    private int code;
    private Message result;
}
