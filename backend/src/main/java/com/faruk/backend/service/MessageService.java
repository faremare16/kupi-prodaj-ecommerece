package com.faruk.backend.service;

import com.faruk.backend.dto.ConversationResponseDto;
import com.faruk.backend.dto.MessageRequestDto;
import com.faruk.backend.dto.MessageResponseDto;
import com.faruk.backend.entity.Message;
import com.faruk.backend.entity.User;
import com.faruk.backend.repository.MessageRepository;
import com.faruk.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageResponseDto sendMessage(String senderUsername, MessageRequestDto requestDto){
        User sender=userRepository.findByUsername(senderUsername)
                .orElseThrow(()->new RuntimeException("Sender not found"));

        User receiver=userRepository.findByUsername(requestDto.getReceiverUsername())
                .orElseThrow(()->new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .content(requestDto.getContent())
                .timeStamp(LocalDateTime.now())
                .sender(sender)
                .receiver(receiver)
                .build();

        Message savedMessage = messageRepository.save(message);

        return mapToDto(savedMessage);
    }

    public List<MessageResponseDto> getChatHistory(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));

        User user2 = userRepository.findByUsername(username2)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        List<Message> messages = messageRepository.findChatHistory(user1.getUsername(), user2.getUsername());

        return messages.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ConversationResponseDto> getConversations(String currentUsername) {
        // pronadje sve korisnike sa kim se korisnik dopisivap
        List<String> partnerUsernames = messageRepository.findDistinctChatPartners(currentUsername);

        List<ConversationResponseDto> conversations = new ArrayList<>();

       // za svakog nadje profilnu sliku i stavi u dtp
        for (String partnerName : partnerUsernames) {
            User partner = userRepository.findByUsername(partnerName).orElse(null);
            String profileImage = (partner != null) ? partner.getProfileImageUrl() : null;

            conversations.add(new ConversationResponseDto(partnerName, profileImage));
        }

        return conversations;
    }

    // pomocna metoda za pretvaranje entity-a u dto
    private MessageResponseDto mapToDto(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .timeStamp(message.getTimeStamp())
                .senderUsername(message.getSender().getUsername())
                .receiverUsername(message.getReceiver().getUsername())
                .build();
    }
}

