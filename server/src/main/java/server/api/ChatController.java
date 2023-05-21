package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.commons.Chat;
import server.commons.ChatUser;
import server.database.ChatRepository;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private ChatRepository repo;

    public ChatController(ChatRepository repo){
        this.repo = repo;
    }

    @PostMapping("/create")
    public ResponseEntity<Chat> createChat(ChatUser initiator, ChatUser receiver){
        Chat chat = new Chat(initiator, receiver);

        return ResponseEntity.ok(chat);
    }
}
