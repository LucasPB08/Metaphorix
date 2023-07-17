package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

@Entity
public class GroupParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private GroupChat chatId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ChatUser userId;

    private Timestamp joinedTime;

    public GroupParticipant(){

    }

    public GroupParticipant(Timestamp joinedTime){
        this.joinedTime = joinedTime;
    }

    public Long getId() {
        return id;
    }

    @JoinColumn
    public GroupChat getChatId() {
        return chatId;
    }

    public ChatUser getUserId() {
        return userId;
    }

    public Timestamp getJoinedTime() {
        return joinedTime;
    }

    public void setChatId(GroupChat chatId) {
        this.chatId = chatId;
    }

    public void setUserId(ChatUser userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString(){
        return "GroupParticipant = {" +
                "id = " + id + "\n" +
                "chat = " + chatId + "\n" +
                "user = " + userId + "\n" +
                "time_joined = " + joinedTime.toString() +  "}";
    }
}
