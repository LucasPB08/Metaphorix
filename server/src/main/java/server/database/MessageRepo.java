package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.commons.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
