package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.commons.Chat;
import server.commons.ChatUser;
import server.commons.Message;
import server.database.ChatRepository;
import server.database.ChatUserRepository;
import server.database.MessageRepo;

import java.util.Optional;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private ChatRepository repo;
    private ChatUserRepository userRepo;
    private MessageRepo messageRepo;

    public ChatController(ChatRepository repo, ChatUserRepository userRepo, MessageRepo messageRepo){
        this.repo = repo;
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
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
    public ResponseEntity<Message> saveMessage(@RequestParam Long chatId, @RequestParam String userId, @RequestBody String message){
        Message messageToSave = new Message(message);

        Optional<ChatUser> optionalChatUser = userRepo.findById(userId);
        Optional<Chat> optionalChat = repo.findById(chatId);

        if(optionalChat.isEmpty() || optionalChatUser.isEmpty()) return ResponseEntity.badRequest().build();

        Chat chat = optionalChat.get();
        ChatUser user = optionalChatUser.get();

        messageToSave.setChat(chat);
        messageToSave.setSender(user);

        chat.addMessage(messageToSave);

        repo.save(chat);
        messageRepo.save(messageToSave);

        return ResponseEntity.ok(messageToSave);
    }
}
