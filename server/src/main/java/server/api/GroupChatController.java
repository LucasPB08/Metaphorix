package server.api;

import commons.ChatUser;
import commons.GroupChat;
import commons.GroupMessage;
import commons.GroupParticipant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Clock;
import server.database.ChatUserRepository;
import server.database.GroupChatRepository;
import server.database.GroupMessageRepository;
import server.database.GroupParticipantRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupChatController {

    private final GroupChatRepository repo;
    private final ChatUserRepository chatUserRepo;
    private final GroupParticipantRepository participantRepo;
    private final GroupMessageRepository messageRepo;
    private final Clock clock;

    /**
     * Constructor
     * @param repo Group chat repository
     * @param chatUserRepository Chat user repository.
     * @param participantRepo Group participants repository.
     * @param messageRepo Group message repository.
     * @param clock Clock
     */
    public GroupChatController(GroupChatRepository repo,
                               ChatUserRepository chatUserRepository,
                               GroupParticipantRepository participantRepo,
                               GroupMessageRepository messageRepo,
                               Clock clock){
        this.repo = repo;
        this.chatUserRepo = chatUserRepository;
        this.participantRepo = participantRepo;
        this.messageRepo = messageRepo;
        this.clock = clock;
    }

    /**
     * Endpoint to create a new group chat and persist it.
     * @param creatorId Username of the creator.
     * @param groupName Name of the group.
     * @param groupDesc Description of the group.
     * @param addedUsersIds The initially added users.
     * @return The saved group chat.
     */
    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroupChat(@RequestParam String creatorId,
                                                     @RequestParam String groupName,
                                                     @RequestBody (required = false)
                                                         String groupDesc,
                                                     @RequestParam(required = false)
                                                         String... addedUsersIds){
        Optional<ChatUser> creator = chatUserRepo.findById(creatorId);

        if(creator.isEmpty()) return ResponseEntity.badRequest().build();

        Timestamp timeCreated = clock.now();

        GroupChat savedGroupChat = repo.save(new GroupChat(timeCreated,groupName));
        savedGroupChat.setGroupDescription(groupDesc);

        GroupParticipant creatorOfGroup = new GroupParticipant(clock.now());
        creatorOfGroup.setChatId(savedGroupChat);
        creatorOfGroup.setUserId(creator.get());
        participantRepo.save(creatorOfGroup);

        if(addedUsersIds != null)
            addParticipantsToChat(savedGroupChat, addedUsersIds);


        return ResponseEntity.ok(savedGroupChat);
    }

    /**
     * Endpoint to retrieve all the participants of a group
     * @param groupChatId The id of the group chat.
     * @return List of the participants.
     */
    @GetMapping("/participants")
    public List<GroupParticipant> participantsOfGroupChat(@RequestParam Long groupChatId){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupChatId);
        if(optionalGroupChat.isEmpty()) return null;

        GroupChat groupChat = optionalGroupChat.get();

        return groupChat.getGroupParticipants();
    }

    /**
     * Endpoint to retrieve all the messages sent to a group chat.
     * @param groupId The id of the group.
     * @return List of messages.
     */
    @GetMapping("/messages")
    public List<GroupMessage> getMessagesOfChat(@RequestParam Long groupId){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupId);
        if(optionalGroupChat.isEmpty()) return null;

        GroupChat groupChat = optionalGroupChat.get();

        return groupChat.getGroupMessages();
    }

    /**
     * Endpoint to save a message sent to a group.
     * @param groupId The id of the group where the message was sent to.
     * @param senderId The username of the sender.
     * @param message The content of the message.
     * @return The group message that was saved to the database.
     */
    @PostMapping("/messages")
    public ResponseEntity<GroupMessage> sendMessage(@RequestParam Long groupId,
                                                    @RequestParam String senderId,
                                                    @RequestBody String message){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupId);

        Optional<GroupParticipant> optionalSender =
                participantRepo.findParticipantOfUser(senderId, groupId);

        if(optionalGroupChat.isEmpty() ||
                optionalSender.isEmpty()) return ResponseEntity.badRequest().build();

        GroupChat groupChat = optionalGroupChat.get();
        GroupParticipant participant = optionalSender.get();

        GroupMessage groupMessage = new GroupMessage(message);
        Timestamp sentAt = clock.now();
        groupMessage.setTimestampSent(sentAt);

        groupMessage.setGroupChat(groupChat);
        groupMessage.setSender(participant);

        messageRepo.save(groupMessage);

        return ResponseEntity.ok(groupMessage);
    }

    /**
     * Endpoint to retrieve a single group chat.
     * @param chatId The id of the chat.
     * @return The group chat.
     */
    @GetMapping("/chat")
    public GroupChat getGroupChatById(@RequestParam Long chatId){
        Optional<GroupChat> optionalGroupChat = repo.findById(chatId);
        if(optionalGroupChat.isEmpty()) return null;

        return optionalGroupChat.get();
    }

    /**
     * Endpoint to edit the description of a group chat.
     * @param chatId The id of the group chat
     * @param description The new description.
     * @return The new description.
     */
    @PutMapping("/description")
    public ResponseEntity<String> editDescription(@RequestParam Long chatId,
                                                  @RequestBody(required = false)
                                                  String description){
        Optional<GroupChat> optionalGroupChat = repo.findById(chatId);

        if(optionalGroupChat.isEmpty()) return ResponseEntity.badRequest().build();

        GroupChat groupChat = optionalGroupChat.get();
        groupChat.setGroupDescription(description);

        repo.save(groupChat);

        return ResponseEntity.ok(description);
    }

    /**
     * Endpoint to delete a group chat from the database.
     * @param chatId The id of the group chat to be deleted.
     * @return True if the group was deleted.
     */
    @DeleteMapping("/chat")
    public ResponseEntity<Boolean> deleteGroupChat(@RequestParam Long chatId){
        if(!repo.existsById(chatId))
            return ResponseEntity.badRequest().build();

        repo.deleteById(chatId);

        return ResponseEntity.ok(true);
    }

    /**
     * Endpoint to remove a participant from a group chat.
     * @param chatId The id of the relevant group chat.
     * @param userName The username of the participant.
     * @return The removed group participant.
     */
    @PutMapping("/remove-participant")
    public ResponseEntity<GroupParticipant> removeParticipant(@RequestParam Long chatId,
                                                              @RequestBody String userName){
        Optional<GroupParticipant> optionalGroupParticipant =
                participantRepo.findParticipantOfUser(userName, chatId);

        if(optionalGroupParticipant.isEmpty()) return ResponseEntity.badRequest().build();

        GroupParticipant groupParticipant = optionalGroupParticipant.get();

        groupParticipant.setChatId(null);
        participantRepo.save(groupParticipant);

        return ResponseEntity.ok(groupParticipant);
    }

    /**
     * Endpoint to add participants to an existing group chat.
     * @param chatId The id to add participants to
     * @param userIds The usernames of the participants to add.
     * @return The group chat that was added to.
     */
    @PostMapping("/participants")
    public ResponseEntity<GroupChat> addParticipants(@RequestParam Long chatId,
                                                     @RequestParam String... userIds){
        Optional<GroupChat> optionalGroupChat = repo.findById(chatId);

        if(optionalGroupChat.isEmpty())
            return ResponseEntity.badRequest().build();

        GroupChat groupChat = optionalGroupChat.get();

        addParticipantsToChat(groupChat, userIds);

        return ResponseEntity.ok(groupChat);
    }

    private void addParticipantsToChat(GroupChat chat, String... userIds){
        Timestamp joined = clock.now();

        for(String id: userIds){

            Optional<ChatUser> user = chatUserRepo.findById(id);
            if(user.isEmpty()) continue;

            GroupParticipant participant = new GroupParticipant(joined);
            participant.setChatId(chat);
            participant.setUserId(user.get());

            participantRepo.save(participant);
        }
    }
}
