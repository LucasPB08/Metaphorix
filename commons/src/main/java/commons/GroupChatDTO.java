package commons;

public class GroupChatDTO {

    private Long id;

    private String groupName;

    private String groupDesc;

    /**
     * Constructor for object mapper
     */
    public GroupChatDTO(){

    }

    /**
     * Constructor used for creating read-only objects as projections
     * of group chats.
     * @param id The id of the group chat
     * @param groupName The name of the group chat
     * @param groupDesc The description of the group chat
     */
    public GroupChatDTO(Long id, String groupName, String groupDesc){
        this.id = id;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
    }

    /**
     * Gets the id
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the group name
     * @return The name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Gets the group description
     * @return The description
     */
    public String getGroupDesc() {
        return groupDesc;
    }

    /**
     * Sets the id
     * @param id The new Id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the group's name
     * @param groupName The new name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Sets the group description
     * @param groupDesc The new description
     */
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
