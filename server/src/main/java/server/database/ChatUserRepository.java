package server.database;


import commons.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import commons.GroupChatDTO;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {

    /**
     * Custom query to retrieve all group chats associated with a user.
     * @param userId The user
     * @return List of read-only projections of the group chats
     */
    @Query("SELECT new commons.GroupChatDTO(GC.id, GC.groupName, GC.groupDescription) " +
            "FROM GroupChat GC " +
            "JOIN GroupParticipant GP ON GP.chatId = GC.id " +
            "JOIN ChatUser CU ON CU.userName = GP.userId " +
            "WHERE CU.userName = :user")
    List<GroupChatDTO> findAllGroupChats(@Param("user") String userId);

}
