package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL)
    private List<GroupParticipant> groupParticipants;

    private String groupName;

    private String groupDescription;

    private Timestamp timeCreated;

    @OneToMany(mappedBy = "groupChat", cascade = CascadeType.ALL)
    private List<GroupMessage> groupMessages;

    /**
     * Constructor for object mapper.
     */
    public GroupChat(){

    }

    /**
     * Constructor
     * @param timeCreated Time of creation.
     * @param groupName Name of the group chat.
     */
    public GroupChat(Timestamp timeCreated, String groupName){
        this.timeCreated = timeCreated;
        this.groupName = groupName;
        this.groupParticipants = new ArrayList<>();
        this.groupMessages = new ArrayList<>();
    }

    /**
     * Gets the id of the group.
     * @return Id of the group.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the participants of the group.
     * @return List of participants
     */
    public List<GroupParticipant> getGroupParticipants() {
        return groupParticipants;
    }

    /**
     * Gets the name of the group.
     * @return The name of the group.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @return The description of the group.
     */
    public String getGroupDescription() {
        return groupDescription;
    }

    /**
     * @return The time of creation of this group.
     */
    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    /**
     * @return The messages sent to this group.
     */
    @JsonIgnore
    public List<GroupMessage> getGroupMessages(){
        return groupMessages;
    }

    /**
     * Sets the name of this group.
     * @param groupName The new name of the group.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Sets the description of this group.
     * @param groupDescription The new description.
     */
    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    /**
     * Adds participants to this group.
     * @param participantsToAdd The new participants.
     */
    public void addParticipants(List<GroupParticipant> participantsToAdd){
        this.groupParticipants.addAll(participantsToAdd);
    }

    /**
     * Checks for equality between this group and another object.
     * @param o Other object.
     * @return True if the objects are equals,
     * false otherwise.
     */
    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Hashes this object.
     * @return  hashed object.
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Makes a string representation of this group.
     * @return The string representation.
     */
    @Override
    public String toString(){
        return "Group_chat = { id = " + this.id + "\n" +
                "group_name = " + this.groupName + "\n" +
                "group_desc = " + this.groupDescription + "\n" +
                "time_created = " + this.timeCreated.toString() + "}";
    }
}
