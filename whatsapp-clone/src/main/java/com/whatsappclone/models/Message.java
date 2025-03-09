package com.whatsappclone.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many messages can be sent by a user; each message has one sender.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // Each message has one recipient.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    // For group messages, set the group; this field is optional.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupChat groupChat;

    @Column(length = 500)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    private String mediaUrl;    // Path or URL to the uploaded media file
    private String mediaType;   // e.g., "image", "video", "audio", "file", "link"

}
