package com.whatsappclone.repositories;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    List<UserProfile> findByUser_NameContainingIgnoreCase(String name);
}
