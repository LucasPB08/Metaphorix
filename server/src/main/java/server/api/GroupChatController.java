package server.api;

import commons.ChatUser;
import commons.GroupChat;
import commons.GroupParticipant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.ChatUserRepository;
import server.database.GroupChatRepository;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class GroupChatController {

    private GroupChatRepository repo;
    private ChatUserRepository chatUserRepo;

    public GroupChatController(GroupChatRepository repo, ChatUserRepository chatUserRepository){
        this.repo = repo;
        this.chatUserRepo = chatUserRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroupChat(@RequestParam String creatorId,
                                                     @RequestParam String groupName,
                                                     @RequestParam List<String> addedUsersIds){
        Optional<ChatUser> creator = chatUserRepo.findById(creatorId);

        if(creator.isEmpty()) return ResponseEntity.badRequest().build();

        GroupParticipant creatorOfGroup = new GroupParticipant(new Timestamp(System.currentTimeMillis()));

        List<GroupParticipant> participants = new ArrayList<>();
        participants.add(creatorOfGroup);

        List<GroupParticipant> addedByCreator = makeListOfParticipants(addedUsersIds);
        participants.addAll(addedByCreator);

        Timestamp timeCreated = new Timestamp(System.currentTimeMillis());
        GroupChat savedGroupChat = repo.save(new GroupChat(timeCreated,groupName, participants));

        return ResponseEntity.ok(savedGroupChat);
    }

    private List<GroupParticipant> makeListOfParticipants(List<String> userIds){
        List<GroupParticipant> toReturn = new ArrayList<>();

        for(String id: userIds){
            Timestamp joined = new Timestamp(System.currentTimeMillis());
            GroupParticipant participant = new GroupParticipant(joined);
            toReturn.add(participant);
        }

        return toReturn;
    }
}
