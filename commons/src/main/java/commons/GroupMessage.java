package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_chat_id")
    private GroupChat groupChat;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private GroupParticipant sender;

    private Timestamp timestampSent;

    private String message;

    public GroupMessage(String message){
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getTimestampSent() {
        return timestampSent;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestampSent(Timestamp timestampSent){
        this.timestampSent = timestampSent;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    public void setSender(GroupParticipant sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public String toString(){
        return "Group Message = {Message: " + message + "}";
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
