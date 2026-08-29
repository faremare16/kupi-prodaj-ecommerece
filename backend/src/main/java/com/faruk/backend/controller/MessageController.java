package com.faruk.backend.controller;

import com.faruk.backend.dto.ConversationResponseDto;
import com.faruk.backend.dto.MessageRequestDto;
import com.faruk.backend.dto.MessageResponseDto;
import com.faruk.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(@Payload MessageRequestDto requestDto, Principal principal){
        String senderUsername=principal.getName();

        // sacuva poruku u bazu i vrati dto
        MessageResponseDto savedMessage=messageService.sendMessage(senderUsername, requestDto);

        // posalje poruku u realnom vremenu dobivaocu
        messagingTemplate.convertAndSendToUser(
                requestDto.getReceiverUsername(),
                "/topic/messages",
                savedMessage
        );

        // posalje poruku nazad i primaouc da se i njemu prikaze
        messagingTemplate.convertAndSendToUser(
                senderUsername,
                "/topic/messages",
                savedMessage

        );
    }

    @GetMapping("/history/{recipientUsername}")
    public ResponseEntity<List<MessageResponseDto>> getHistory(
            @PathVariable String recipientUsername,
            Principal principal
    ) {
        String currentUser=principal.getName();
        List<MessageResponseDto> history=messageService.getChatHistory(currentUser, recipientUsername);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponseDto>> getConversations(Principal principal) {
        String currentUsername = principal.getName();
        List<ConversationResponseDto> conversations = messageService.getConversations(currentUsername);
        return ResponseEntity.ok(conversations);
    }
}
