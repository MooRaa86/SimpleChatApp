# Spring Boot WebSocket Chat Application

A real-time chat application built using **Spring Boot**, **WebSocket**, **STOMP**, and **SockJS**.  
The application allows multiple users to join a chat room, send messages instantly, and receive join/leave notifications in real time.

---

## Features

- ✅ Real-time messaging using WebSocket  
- 👥 Multiple users in one chat room  
- 🔔 Join & Leave notifications  
- 🔄 STOMP messaging protocol  
- 🧦 SockJS fallback support  
- 🧩 Clean separation between Config, Controller, and Listener layers  

---

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring WebSocket**
- **STOMP Protocol**
- **SockJS**
- **HTML / CSS / JavaScript**

---

## WebSocket Flow (How It Works)

1. Client connects to WebSocket endpoint:
   ```
   /ws
   ```

2. Messages sent from client to server:
   ```
   /app/chat.sendMessage
   /app/chat.addUser
   ```

3. Server broadcasts messages to all clients:
   ```
   /topic/chat
   ```

4. All subscribed users receive messages in real time.

---

## WebSocket Configuration

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
```

---

## Chat Controller

Handles sending messages and registering users.

```java
@MessageMapping("/chat.sendMessage")
@SendTo("/topic/chat")
public MessageDto sendMessage(@Payload MessageDto message) {
    return message;
}
```

```java
@MessageMapping("/chat.addUser")
@SendTo("/topic/chat")
public MessageDto addUser(@Payload MessageDto message,
                          SimpMessageHeaderAccessor headerAccessor) {
    headerAccessor.getSessionAttributes().put("username", message.getSender());
    return message;
}
```

---

## WebSocket Disconnect Listener

- Detects when a user disconnects  
- Broadcasts a **LEAVE** event to all connected users  

```java
@EventListener
public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String username = (String) accessor.getSessionAttributes().get("username");

    if (username != null) {
        messagingTemplate.convertAndSend("/topic/chat",
                MessageDto.builder()
                        .msgType(MsgType.LEAVE)
                        .sender(username)
                        .build());
    }
}
```

---

## How to Run the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/chat-app.git
   ```

2. Navigate to the project directory:
   ```bash
   cd chat-app
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Open your browser:
   ```
   http://localhost:8080
   ```

---

## Testing Tips

- Open the application in **two different browsers**
- Enter different usernames
- Send messages and observe real-time updates
- Close one tab to test **disconnect (LEAVE) events**


