package com.chat.app.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Service
public class UserService {

    private final ConcurrentHashMap<String, String> activeUsers = new ConcurrentHashMap<>();

    public void addUser(String sessionId, String username) {
        activeUsers.put(sessionId, username);
    }

    public String removeUser(String sessionId) {
        return activeUsers.remove(sessionId);
    }

    public Set<String> getActiveUsernames() {
        return Set.copyOf(activeUsers.values());
    }

    public int getActiveUserCount() {
        return activeUsers.size();
    }

    public boolean isUserActive(String username) {
        return activeUsers.containsValue(username);
    }
}