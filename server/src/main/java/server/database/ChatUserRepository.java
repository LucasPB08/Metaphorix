package server.database;


import commons.ChatUser;
import commons.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import commons.GroupChatDTO;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {

    @Query("SELECT new commons.GroupChatDTO(GC.id, GC.groupName, GC.groupDescription) " +
            "FROM GroupChat GC " +
            "JOIN GroupParticipant GP ON GP.chatId = GC.id " +
            "JOIN ChatUser CU ON CU.userName = GP.userId " +
            "WHERE CU.userName = :user")
    List<GroupChatDTO> findAllGroupChats(@Param("user") String userId);

    @Query("SELECT gp FROM GroupParticipant gp " +
            "INNER JOIN ChatUser u ON gp.userId = u.userName " +
            "WHERE u.userName = :userId AND " +
            "gp.chatId.id = :chatId")
    GroupParticipant findParticipantOfUser(@Param("userId") String userId,
                                           @Param("chatId") Long groupChatId);
}
