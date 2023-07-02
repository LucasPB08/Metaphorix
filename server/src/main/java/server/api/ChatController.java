package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.commons.Chat;
import server.commons.ChatUser;
import server.commons.Message;
import server.database.ChatRepository;
import server.database.ChatUserRepository;
import server.database.MessageRepo;

import java.util.List;
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

        ChatUser initiator = persistedInitiator.get();
        ChatUser receiver = persistedReceiver.get();

        Chat savedChat = repo.save(new Chat(initiator, receiver));

        initiator.addInitiatedChat(savedChat);
        receiver.addReceivedChat(savedChat);

        userRepo.save(initiator);
        userRepo.save(receiver);

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

        repo.save(chat);
        messageRepo.save(messageToSave);

        return ResponseEntity.ok(messageToSave);
    }

    @GetMapping()
    public List<Message> getMessages(@RequestParam Long chatId){
        Optional<Chat> optionalChat = repo.findById(chatId);

        if(optionalChat.isEmpty()) return null;

        Chat chat = optionalChat.get();

        return chat.getMessages();
    }
}
