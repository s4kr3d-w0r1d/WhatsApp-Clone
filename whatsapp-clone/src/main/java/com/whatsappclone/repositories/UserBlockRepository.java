package com.whatsappclone.repositories;

import com.whatsappclone.models.User;
import com.whatsappclone.models.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    // Find a block record between a blocker and a blocked user.
    Optional<UserBlock> findByBlockerAndBlocked(User blocker, User blocked);

    // Retrieve all block records for a given blocker.
    List<UserBlock> findByBlocker(User blocker);
}
