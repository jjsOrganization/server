package com.jjs.ClothingInventorySaleReformPlatform.domain.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@Table(name = "ChatMessage")
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long MessageId;

    private String sender;

    private String message;

    private String sentAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROOM_ID")
    private Chat chat;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        this.sentAt = formattedTime;
    }
}
