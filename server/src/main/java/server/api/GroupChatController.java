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
import server.database.GroupParticipantRepository;

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
                                                     @RequestParam String... addedUsersIds){
        Optional<ChatUser> creator = chatUserRepo.findById(creatorId);

        if(creator.isEmpty()) return ResponseEntity.badRequest().build();

        Timestamp timeCreated = new Timestamp(System.currentTimeMillis());
        GroupChat savedGroupChat = repo.save(new GroupChat(timeCreated,groupName));

        GroupParticipant creatorOfGroup = new GroupParticipant(new Timestamp(System.currentTimeMillis()));
        creatorOfGroup.setChatId(savedGroupChat);
        creatorOfGroup.setUserId(creator.get());
        participantRepo.save(creatorOfGroup);

        List<GroupParticipant> participants = new ArrayList<>();
        participants.add(creatorOfGroup);

        List<GroupParticipant> addedByCreator = makeListOfParticipants(savedGroupChat, addedUsersIds);
        participants.addAll(addedByCreator);

        savedGroupChat.addParticipants(participants);

        return ResponseEntity.ok(savedGroupChat);
    }

    private List<GroupParticipant> makeListOfParticipants(GroupChat chat, String... userIds){
        List<GroupParticipant> toReturn = new ArrayList<>();

        for(String id: userIds){
            Timestamp joined = new Timestamp(System.currentTimeMillis());

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
