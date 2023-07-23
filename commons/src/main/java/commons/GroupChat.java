package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "chatId")
    private List<GroupParticipant> groupParticipants;

    private String groupName;

    private String groupDescription;

    private Timestamp timeCreated;

    public GroupChat(){

    }

    public GroupChat(Long id, String groupName, String groupDesc){
        this.id = id;
        this.groupName = groupName;
        this.groupDescription = groupDesc;
    }

    public GroupChat(Timestamp timeCreated, String groupName){
        this.timeCreated = timeCreated;
        this.groupName = groupName;
        this.groupParticipants = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public List<GroupParticipant> getGroupParticipants() {
        return groupParticipants;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public void addParticipants(List<GroupParticipant> participantsToAdd){
        this.groupParticipants.addAll(participantsToAdd);
    }

    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString(){
        return "Group_chat = { id = " + this.id + "\n" +
                "group_name = " + this.groupName + "\n" +
                "group_desc = " + this.groupDescription + "\n" +
                "time_created = " + this.timeCreated.toString() + "}";
    }
}
