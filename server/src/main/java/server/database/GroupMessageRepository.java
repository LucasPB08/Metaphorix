package server.database;

import commons.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
}
