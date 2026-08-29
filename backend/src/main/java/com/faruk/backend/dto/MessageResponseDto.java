package com.faruk.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime timeStamp;
    private String senderUsername;
    private String receiverUsername;
}
