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
import java.util.ArrayList;
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

    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroupChat(@RequestParam String creatorId,
                                                     @RequestParam String groupName,
                                                     @RequestBody (required = false) String groupDesc,
                                                     @RequestParam (required = false) String... addedUsersIds){
        Optional<ChatUser> creator = chatUserRepo.findById(creatorId);

        if(creator.isEmpty()) return ResponseEntity.badRequest().build();

        Timestamp timeCreated = clock.now();

        GroupChat savedGroupChat = repo.save(new GroupChat(timeCreated,groupName));
        savedGroupChat.setGroupDescription(groupDesc);

        GroupParticipant creatorOfGroup = new GroupParticipant(new Timestamp(System.currentTimeMillis()));
        creatorOfGroup.setChatId(savedGroupChat);
        creatorOfGroup.setUserId(creator.get());
        participantRepo.save(creatorOfGroup);

        if(addedUsersIds != null)
            addParticipantsToChat(savedGroupChat, addedUsersIds);


        return ResponseEntity.ok(savedGroupChat);
    }

    @GetMapping("/participants")
    public List<GroupParticipant> participantsOfGroupChat(@RequestParam Long groupChatId){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupChatId);
        if(optionalGroupChat.isEmpty()) return null;

        GroupChat groupChat = optionalGroupChat.get();

        return groupChat.getGroupParticipants();
    }

    @GetMapping("/messages")
    public List<GroupMessage> getMessagesOfChat(@RequestParam Long groupId){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupId);
        if(optionalGroupChat.isEmpty()) return null;

        GroupChat groupChat = optionalGroupChat.get();

        return groupChat.getGroupMessages();
    }

    @PostMapping("/messages")
    public ResponseEntity<GroupMessage> sendMessage(@RequestParam Long groupId,
                                                    @RequestParam String senderId,
                                                    @RequestBody String message){
        Optional<GroupChat> optionalGroupChat = repo.findById(groupId);
        Optional<GroupParticipant> optionalSender = participantRepo.findParticipantOfUser(senderId, groupId);

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

    @GetMapping("/chat")
    public GroupChat getGroupChatById(@RequestParam Long chatId){
        Optional<GroupChat> optionalGroupChat = repo.findById(chatId);
        if(optionalGroupChat.isEmpty()) return null;

        return optionalGroupChat.get();
    }

    @PutMapping("/description")
    public ResponseEntity<String> editDescription(@RequestParam Long chatId,
                                                  @RequestBody(required = false) String description){
        Optional<GroupChat> optionalGroupChat = repo.findById(chatId);

        if(optionalGroupChat.isEmpty()) return ResponseEntity.badRequest().build();

        GroupChat groupChat = optionalGroupChat.get();
        groupChat.setGroupDescription(description);

        repo.save(groupChat);

        return ResponseEntity.ok(description);
    }

    @DeleteMapping("/chat")
    public ResponseEntity<Boolean> deleteGroupChat(@RequestParam Long chatId){
        if(!repo.existsById(chatId))
            return ResponseEntity.badRequest().build();

        repo.deleteById(chatId);

        return ResponseEntity.ok(true);
    }

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
