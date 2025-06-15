package com.chat.app.controller;

import com.chat.app.model.ChatMessage;
import com.chat.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class ChatController {

    private static final String[] COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7",
            "#DDA0DD", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E9"
    };

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        message.setType(ChatMessage.MessageType.CHAT);

        // Assign a color if not already set
        if (message.getSenderColor() == null || message.getSenderColor().isEmpty()) {
            message.setSenderColor(getRandomColor());
        }

        return message;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/messages")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        String sessionId = headerAccessor.getSessionId();

        // Add user to active users
        userService.addUser(sessionId, message.getSender());

        message.setTimestamp(LocalDateTime.now());
        message.setType(ChatMessage.MessageType.JOIN);
        message.setContent(message.getSender() + " joined the chat!");
        message.setSenderColor(getRandomColor());

        // Broadcast updated user count
        broadcastUserCount();

        return message;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public ChatMessage userTyping(@Payload ChatMessage message) {
        message.setType(ChatMessage.MessageType.TYPING);
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    private String getRandomColor() {
        Random random = new Random();
        return COLORS[random.nextInt(COLORS.length)];
    }

    private void broadcastUserCount() {
        int userCount = userService.getActiveUserCount();
        ChatMessage userCountMessage = new ChatMessage();
        userCountMessage.setType(ChatMessage.MessageType.USER_COUNT);
        userCountMessage.setContent(String.valueOf(userCount));
        userCountMessage.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/userCount", userCountMessage);
    }
}