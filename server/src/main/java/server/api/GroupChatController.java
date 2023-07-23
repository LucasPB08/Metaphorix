package server.api;

import commons.ChatUser;
import commons.GroupChat;
import commons.GroupParticipant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ChatUserRepository;
import server.database.GroupChatRepository;
import server.database.GroupParticipantRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupChatController {

    private GroupChatRepository repo;
    private ChatUserRepository chatUserRepo;
    private GroupParticipantRepository participantRepo;

    public GroupChatController(GroupChatRepository repo,
                               ChatUserRepository chatUserRepository,
                               GroupParticipantRepository participantRepo){
        this.repo = repo;
        this.chatUserRepo = chatUserRepository;
        this.participantRepo = participantRepo;
    }

    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroupChat(@RequestParam String creatorId,
                                                     @RequestParam String groupName,
                                                     @RequestBody (required = false) String groupDesc,
                                                     @RequestParam (required = false) String... addedUsersIds){
        Optional<ChatUser> creator = chatUserRepo.findById(creatorId);

        if(creator.isEmpty()) return ResponseEntity.badRequest().build();

        Timestamp timeCreated = new Timestamp(System.currentTimeMillis());

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

    private List<GroupParticipant> addParticipantsToChat(GroupChat chat, String... userIds){
        List<GroupParticipant> toReturn = new ArrayList<>();
        Timestamp joined = new Timestamp(System.currentTimeMillis());

        for(String id: userIds){

            Optional<ChatUser> user = chatUserRepo.findById(id);
            if(user.isEmpty()) continue;

            GroupParticipant participant = new GroupParticipant(joined);
            participant.setChatId(chat);
            participant.setUserId(user.get());

            participantRepo.save(participant);

            toReturn.add(participant);
        }

        return toReturn;
    }
}
