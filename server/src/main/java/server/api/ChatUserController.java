package server.api;

import org.springframework.http.ResponseEntity;
import server.commons.ChatUser;
import org.springframework.web.bind.annotation.*;
import server.database.ChatUserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class ChatUserController {
    private final ChatUserRepository repo;

    /**
     * Constructor for this controller
     * @param repo
     */
    public ChatUserController(ChatUserRepository repo){
        this.repo = repo;
    }

    /**
     * Stores a new user in the database
     * @param fullName full name of user
     * @param userName user name
     * @param password password of user
     * @return OK status
     */
    @PostMapping("/store")
    public ResponseEntity<ChatUser> storeUser(String fullName, String userName, String password){
        ChatUser u = new ChatUser(userName, fullName, password);
        repo.save(u);

        return ResponseEntity.ok(u);
    }

    @GetMapping("/user/{id}")
    public Boolean existsUser(@PathVariable("id") String id){
        return repo.existsById(id);
    }

    @GetMapping("/user")
    public ChatUser getUser(String id){
        Optional<ChatUser> user = repo.findById(id);

        if(user.isEmpty()) return null;

        return user.get();
    }

    @GetMapping("/password")
    public Boolean validatePassword(String user, String password){
        if(!repo.existsById(user)) return false;

        ChatUser u = repo.findById(user).get();

        return u.validatePassword(password);
    }

    /**
     * Gets all the users in the database
     * @return List of all users
     */
    @GetMapping("/")
    public List<ChatUser> getAll(){
        return repo.findAll();
    }
}
