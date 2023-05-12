package server.api;

import org.springframework.http.ResponseEntity;
import server.commons.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.ChatUserRepository;

import java.util.List;

@RestController
@RequestMapping("/user")
public class ChatUserController {
    private final ChatUserRepository repo;

    public ChatUserController(ChatUserRepository repo){
        this.repo = repo;
    }

    @PostMapping("/{name}")
    public ResponseEntity<ChatUser> storeUser(@PathVariable("name") String name){
        ChatUser u = new ChatUser(name);
        repo.save(u);

        return ResponseEntity.ok(u);
    }

    @GetMapping("/")
    public List<ChatUser> getAll(){
        return repo.findAll();
    }
}
