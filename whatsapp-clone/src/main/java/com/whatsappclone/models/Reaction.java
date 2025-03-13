package com.whatsappclone.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Table(name = "reactions")
@Setter
@Getter
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type of reaction (e.g., "like", "smile", "heart")

    private String type;

    // The message that is reacted to

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    // The user who reacted
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Reaction() {
    }

    public Reaction(String type, Message message, User user) {
        this.type = type;
        this.message = message;
        this.user = user;
    }

    // equals() and hashCode() may be useful when comparing reactions.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reaction)) return false;
        Reaction reaction = (Reaction) o;
        return Objects.equals(id, reaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
