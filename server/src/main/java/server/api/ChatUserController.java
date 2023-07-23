package server.api;

import commons.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ChatUserRepository;
import commons.GroupChatDTO;

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
     * @param userName username
     * @param password password of user
     * @return OK status
     */
    @PostMapping("/store")
    public ResponseEntity<ChatUser> storeUser(String fullName, String userName, String password){
        ChatUser u = new ChatUser(userName, fullName, password);
        repo.save(u);

        return ResponseEntity.ok(u);
    }

    /**
     * Checks whether the user exists
     * @param id username
     * @return true if user exists, false otherwise.
     */
    @GetMapping("/exists")
    public Boolean existsUser(String id){
        return repo.existsById(id);
    }

    /**
     * Gets a user from the database
     * @param id username of the user
     * @return the user
     */
    @GetMapping("/user")
    public ChatUser getUser(String id){
        Optional<ChatUser> user = repo.findById(id);

        if(user.isEmpty()) return null;

        return user.get();
    }

    /**
     * Checks if the password corresponds to the user's password
     * @param user user to check password of
     * @param password password input
     * @return true if passwords match, false otherwise.
     */
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

    /**
     * Gets all the messages from a certain user
     * @param userId Id of the user
     * @return all the messages from the user
     */
    @GetMapping("/messages")
    public List<Message> getMessages(@RequestParam String userId){
        Optional<ChatUser> user = repo.findById(userId);

        if(user.isEmpty()) return null;

        ChatUser chatUser = user.get();

        return chatUser.getMessages();
    }

    /**
     * Gets the chats that a certain user participates in
     * @param userId ID of the user
     * @return the chats that the user participates in
     */
    @GetMapping("/chats")
    public List<Chat> getChats(@RequestParam String userId){
        Optional<ChatUser> optionalChatUser = repo.findById(userId);

        if(optionalChatUser.isEmpty()) return null;

        ChatUser user = optionalChatUser.get();

        return user.allChats();
    }

    @GetMapping("/groups")
    public List<GroupChatDTO> getGroups(@RequestParam String userId){
        Optional<ChatUser> optionalChatUser = repo.findById(userId);

        if(optionalChatUser.isEmpty()) return null;

        return repo.findAllGroupChats(userId);
    }
}
