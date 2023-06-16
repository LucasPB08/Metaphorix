package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.commons.Chat;
import server.commons.ChatKey;
import server.commons.ChatUser;
import server.commons.Message;
import server.database.ChatRepository;
import server.database.ChatUserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private ChatRepository repo;
    private ChatUserRepository userRepo;

    public ChatController(ChatRepository repo, ChatUserRepository userRepo){
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @PostMapping()
    public ResponseEntity<Chat> createChat(@RequestParam String initiatorId, @RequestBody String receiverId){
        Optional<ChatUser> persistedInitiator = userRepo.findById(initiatorId);
        Optional<ChatUser> persistedReceiver = userRepo.findById(receiverId);

        if(persistedInitiator.isEmpty() || persistedReceiver.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Chat savedChat = repo.save(new Chat(persistedInitiator.get(), persistedReceiver.get()));

        return ResponseEntity.ok(savedChat);
    }

    @PutMapping()
    public ResponseEntity<Message> saveMessage(@RequestParam Long chatId, @RequestBody String message){
        Message messageToSave = new Message(message);

        Optional<Chat> optionalChat = repo.findById(chatId);
        if(optionalChat.isEmpty()) return ResponseEntity.badRequest().build();

        Chat chat = optionalChat.get();
        chat.addMessage(messageToSave);

        messageToSave.setChat(chat);

        repo.save(chat);

        return ResponseEntity.ok(messageToSave);
    }
}
