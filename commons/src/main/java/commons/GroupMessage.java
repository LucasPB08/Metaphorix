package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

@Entity
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

    /**
     * Constructor for object mapper.
     */
    public GroupMessage(){

    }

    /**
     * Constructor
     * @param message The content of the message.
     */
    public GroupMessage(String message){
        this.message = message;
    }

    /**
     * Gets the id of this message.
     * @return The id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the time of creation
     * @return Time of creation.
     */
    public Timestamp getTimestampSent() {
        return timestampSent;
    }

    /**
     * Gets the content
     * @return The content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the sender
     * @return The sender.
     */
    public GroupParticipant getSender(){
        return sender;
    }

    /**
     * Gets the group chat where this message was sent to
     * @return The group chat
     */
    public GroupChat getGroupChat(){
        return groupChat;
    }

    /**
     * Sets the time creation
     * @param timestampSent The time of creation
     */
    public void setTimestampSent(Timestamp timestampSent){
        this.timestampSent = timestampSent;
    }

    /**
     * Sets the group chat where this was sent to
     * @param groupChat The group chat
     */
    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    /**
     * Sets the sender of the message
     * @param sender The sender
     */
    public void setSender(GroupParticipant sender) {
        this.sender = sender;
    }

    /**
     * Checks for equality between this and another object
     * @param other Other object
     * @return True if Other equals this,
     * false otherwise.
     */
    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }

    /**
     * Makes a string representation of this object
     * @return The string representation.
     */
    @Override
    public String toString(){
        return "Group Message = {Message: " + message + "}";
    }

    /**
     * Hashes this object.
     * @return Hashed object.
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
