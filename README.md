# WhatsApp Clone 
# MA 104 Project 1

## Project Overview
**WhatsApp Clone** is a full-stack messaging application that replicates the core functionalities of WhatsApp. It supports real-time one-to-one and group messaging with end-to-end encryption, media sharing via relative URLs, message status updates (delivered, read), and options for message deletion (for sender and recipient). The project leverages both HTTP and WebSocket (STOMP) protocols to provide a robust, secure, and interactive chat experience.

## Features
- **Real-Time Messaging:**  
  One-to-one and group chats implemented with WebSockets (STOMP protocol) for instant communication.
- **End-to-End Encryption:**  
  Secure messaging with text and media encryption using Diffie-Hellman key exchange and AES.
- **Media Sharing:**  
  Users can upload media from their computer. The media is stored on the server (e.g., in `C:/uploads/chat`) and is shared via relative URLs.
- **Message Status Updates:**  
  Ability to mark messages as delivered and read.
- **Message Deletion:**  
  Options to delete a message for the current user or for everyone.
- **Chat History:**  
  Retrieve complete conversation history via HTTP endpoints.
- **User Blocking & Management:**  
  Manage unwanted messages and chat groups.
- **Group**
  Users can create 2 types of groups : public and private. In public groups users can request to join groups of their interests after getting approved by admins. While in public groups only admins can add users.
- **User Profile and Staus**
  Users cana dd profile picture , status and bio.

## Technologies & Libraries Used
- **Backend:**  
  - Java, Spring Boot  
  - Spring Data JPA, Hibernate  
  - Spring WebSocket (STOMP), Spring Security, JWT  
  - PostgreSQL  
- **Frontend:**  
  - Vite, React.js  
  - HTML, CSS, JavaScript  
  - SockJS, STOMP.js  
- **Encryption:**  
  - Custom end-to-end encryption using Diffie-Hellman key exchange and AES  
- **Build Tools:**  
  - Maven for backend, Vite for frontend

## Installation & Setup

### Prerequisites
- Java 11 or higher
- PostgreSQL Database
- Maven or Gradle
- Node.js (for frontend using Vite)

### Steps

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/s4kr3d-w0r1d/WhatsApp-Clone.git

### Team Members

1. Saksham Madan (s4kr3d-w0r1d) 
2. Vaishnavi (Celestial-glitch)
3. Tarun Agrawal (PH4NT0M-droid)
