package client.scenes;

import com.google.inject.Inject;
import commons.GroupChat;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GroupChatOverviewController {
    private MainCtrl mainCtrl;

    @FXML
    private Text title;

    @FXML
    private Text groupDescription;

    @Inject
    public GroupChatOverviewController(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    public void setInfo(GroupChat groupChat){
        title.setText(groupChat.getGroupName());
        groupDescription.setText(groupChat.getGroupDescription());
    }

    public void back(){
        mainCtrl.showChatOverview();
    }

}
