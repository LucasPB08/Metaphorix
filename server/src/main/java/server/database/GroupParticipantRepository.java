package server.database;

import commons.GroupParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {

}
