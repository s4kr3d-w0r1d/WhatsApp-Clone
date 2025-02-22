package com.whatsappclone.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;  // References User entity

    @Column
    private String profilePic;  // Stores URL or Base64 string

    @Column
    private String bio;

    @Column
    private String status;
}
