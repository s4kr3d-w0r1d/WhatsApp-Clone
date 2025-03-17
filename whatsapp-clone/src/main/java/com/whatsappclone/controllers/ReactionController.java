package com.whatsappclone.controllers;

import com.whatsappclone.models.Reaction;
import com.whatsappclone.services.ReactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    // POST endpoint to add or update a reaction.
    @PostMapping
    public ResponseEntity<Reaction> addOrUpdateReaction(@RequestParam Long messageId,
                                                        @RequestParam Long userId,
                                                        @RequestParam String type) {
        Reaction reaction = reactionService.addReaction(messageId, userId, type);
        return new ResponseEntity<>(reaction, HttpStatus.CREATED);
    }

    // GET endpoint to retrieve reactions for a message.
    // Example: GET /api/reactions/5
    @GetMapping("/{messageId}")
    public ResponseEntity<List<Reaction>> getReactions(@PathVariable Long messageId) {
        List<Reaction> reactions = reactionService.getReactionsForMessage(messageId);
        return ResponseEntity.ok(reactions);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeReaction(@RequestParam Long messageId,
                                               @RequestParam Long userId) {
        reactionService.removeReaction(messageId, userId);
        return ResponseEntity.noContent().build();
    }
}
