package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.commons.Chat;
import server.commons.ChatKey;

public interface ChatRepository extends JpaRepository<Chat, ChatKey> {
}
