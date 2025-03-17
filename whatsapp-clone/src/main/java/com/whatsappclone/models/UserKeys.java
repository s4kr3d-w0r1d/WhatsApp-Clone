package com.whatsappclone.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Timestamp;
import java.sql.Types;

@Getter
@Setter
@Entity
@Table(name = "user_keys")
public class UserKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We'll store only the user ID (or you can map a relation)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "public_key", nullable = false)
    private byte[] publicKeyBytes;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "private_key", nullable = false)
    private byte[] privateKeyBytes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
