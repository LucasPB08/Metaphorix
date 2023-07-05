package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
