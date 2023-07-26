package server.database;

import commons.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {
    @Query("SELECT gp FROM GroupParticipant gp " +
            "INNER JOIN ChatUser u ON gp.userId = u.userName " +
            "WHERE u.userName = :userId AND " +
            "gp.chatId.id = :chatId")
    Optional<GroupParticipant> findParticipantOfUser(@Param("userId") String userId,
                                                     @Param("chatId") Long groupChatId);
}
