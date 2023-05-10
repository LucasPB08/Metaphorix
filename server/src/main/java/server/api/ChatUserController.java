package server.api;

import commons.ChatUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.ChatUserRepository;

import java.util.List;

@Controller
@RequestMapping("/user")
public class ChatUserController {
    private final ChatUserRepository repo;

    public ChatUserController(ChatUserRepository repo){
        this.repo = repo;
    }

    @PostMapping("/{name}")
    public void storeUser(@PathVariable("name") String name){
        ChatUser u = new ChatUser(name);
        repo.save(u);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ChatUser> getAll(){
        return repo.findAll();
    }
}
