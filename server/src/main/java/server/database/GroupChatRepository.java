package server.database;

import commons.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
}
