package com.whatsappclone.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference  // This side is not serialized (prevents infinite recursion)
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
