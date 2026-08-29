package com.faruk.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable = false)
    private User receiver;
}
