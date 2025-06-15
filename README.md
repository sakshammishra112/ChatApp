# ChatFlow - Real-Time Chat Application

A modern, real-time chat application built with Spring Boot, WebSocket, and STOMP protocol. Features include instant messaging, user presence tracking, typing indicators, and a responsive web interface.

## 🚀 Features

- **Real-time messaging** - Instant message delivery using WebSocket
- **User presence tracking** - See who's online in real-time
- **Typing indicators** - Know when someone is typing
- **User avatars with colors** - Randomly assigned colors for user identification
- **Responsive design** - Works on desktop and mobile devices
- **Connection status** - Visual indicators for connection state
- **System notifications** - Join/leave notifications
- **Message timestamps** - Local time display for all messages

## 🛠️ Technology Stack

### Backend
- **Spring Boot** - Main framework
- **Spring WebSocket** - WebSocket support
- **Spring Messaging** - STOMP protocol implementation
- **Lombok** - Boilerplate code reduction
- **Jackson** - JSON serialization

### Frontend
- **HTML5 & CSS3** - Modern web standards
- **Bootstrap 5** - Responsive UI framework
- **Font Awesome** - Icons
- **SockJS** - WebSocket fallback support
- **STOMP.js** - STOMP client library

## 📁 Project Structure

```
src/main/java/com/chat/app/
├── config/
│   ├── WebSocketConfig.java          # WebSocket configuration
│   └── WebSocketEventListener.java   # Connection event handling
├── controller/
│   └── ChatController.java           # Message routing controller
├── model/
│   └── ChatMessage.java              # Message data model
└── service/
    └── UserService.java              # User management service

src/main/resources/
├── static/
│   ├── chat.css
├── template/                          # Main chat interface
│   └── chat.html                      # Styling (referenced but not included)
└── application.properties             # Spring Boot configuration
```

## 🔧 Configuration

### WebSocket Configuration
- **Endpoint**: `/chat` with SockJS fallback
- **Message Broker**: Simple broker for `/topic` and `/queue`
- **Application Prefix**: `/app`
- **User Destination Prefix**: `/user`

### Message Types
- `CHAT` - Regular chat messages
- `JOIN` - User joined notification
- `LEAVE` - User left notification
- `TYPING` - Typing indicator
- `USER_COUNT` - Online user count updates

## 🚦 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Modern web browser with WebSocket support

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd chatflow
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   Open your browser and navigate to:
   ```
   http://localhost:8080/chat
   ```

### Running Multiple Instances
To test with multiple users, open the application in multiple browser tabs or different browsers.

## 📡 WebSocket Endpoints

### Client → Server
- `/app/addUser` - Join the chat
- `/app/sendMessage` - Send a chat message
- `/app/typing` - Send typing indicator

### Server → Client
- `/topic/messages` - Receive chat messages and notifications
- `/topic/typing` - Receive typing indicators
- `/topic/userCount` - Receive user count updates

## Preview
![image](https://github.com/user-attachments/assets/58b90e09-80b8-4590-ad6b-78def342a57e)


