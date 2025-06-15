package com.chat.app.config;

import com.chat.app.model.ChatMessage;
import com.chat.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // User connected - we'll add them to active users when they join with a username
        broadcastUserCount();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = headerAccessor.getSessionId();

        if (username != null) {
            userService.removeUser(sessionId);

            // Send leave message
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setSender(username);
            leaveMessage.setContent(username + " left the chat");
            leaveMessage.setType(ChatMessage.MessageType.LEAVE);
            leaveMessage.setTimestamp(LocalDateTime.now());

            messagingTemplate.convertAndSend("/topic/messages", leaveMessage);
            broadcastUserCount();
        }
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