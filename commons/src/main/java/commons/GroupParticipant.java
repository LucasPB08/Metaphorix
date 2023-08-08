package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;
import java.util.List;

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

    @OneToMany(mappedBy = "sender")
    private List<GroupMessage> messagesSent;

    private Timestamp joinedTime;

    /**
     * Constructor for object mapper.
     */
    public GroupParticipant(){

    }

    /**
     * Constructor
     * @param joinedTime The time that this participant joined
     *                   the group chat.
     */
    public GroupParticipant(Timestamp joinedTime){
        this.joinedTime = joinedTime;
    }

    /**
     * Gets the id
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the group chat
     * @return The group chat.
     */
    @JsonIgnore
    public GroupChat getChatId() {
        return chatId;
    }

    /**
     * Gets the id of the user
     * @return The id
     */
    public ChatUser getUserId() {
        return userId;
    }

    /**
     * Gets the joined time
     * @return Joined time
     */
    public Timestamp getJoinedTime() {
        return joinedTime;
    }

    /**
     * Sets the chat
     * @param chatId The group chat
     */
    public void setChatId(GroupChat chatId) {
        this.chatId = chatId;
    }

    /**
     * Sets the user
     * @param userId The user
     */
    public void setUserId(ChatUser userId) {
        this.userId = userId;
    }

    /**
     * Checks for equality between this and another object.
     * @param other The other object
     * @return True if the objects are equals, false otherwise.
     */
    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }

    /**
     * Hashes this object.
     * @return Hashed object.
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Makes a string representation of this object.
     * @return The string representation.
     */
    @Override
    public String toString(){
        return "GroupParticipant = {" +
                "id = " + id + "\n" +
                "chat = " + chatId + "\n" +
                "user = " + userId + "\n" +
                "time_joined = " + joinedTime.toString() +  "}";
    }
}
