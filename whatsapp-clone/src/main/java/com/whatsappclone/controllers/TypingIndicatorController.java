package com.whatsappclone.controllers;

import com.whatsappclone.dto.TypingIndicator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TypingIndicatorController {

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public TypingIndicator handleTypingEvent(TypingIndicator typingIndicator) {
        System.out.println("User " + typingIndicator.getUserId() +
                (typingIndicator.isTyping() ? " is typing..." : " stopped typing."));
        return typingIndicator;
    }
}