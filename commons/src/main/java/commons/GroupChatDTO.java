package commons;

public class GroupChatDTO {

    private Long id;

    private String groupName;

    private String groupDesc;

    public GroupChatDTO(Long id, String groupName, String groupDesc){
        this.id = id;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
    }

    public Long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
