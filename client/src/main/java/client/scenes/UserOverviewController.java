package client.scenes;

import client.FXMLBuilder;
import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Pair;

import java.util.Optional;

public class UserOverviewController extends OverviewParent{

    @Inject
    public UserOverviewController(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Adds a chat
     */
    public void addChat(){
        Pair<AddChatsCtrl, Dialog<ButtonType>> pair = new FXMLBuilder(MyApplication.getInjector())
                .buildDialogPane("scenes/add-user-dialog.fxml");
        if(pair == null) return;

        Dialog<ButtonType> dialog = pair.getValue();
        dialog.setTitle("Available users");
        Optional<ButtonType> pressed = dialog.showAndWait();

        if(pressed.isEmpty()) return;

        ButtonType type = pressed.get();
        AddChatsCtrl controller = pair.getKey();

        if(type == ButtonType.OK) {
            String userIdSelected  = controller.getSelected();

            if(userIdSelected != null)
                addUser(userIdSelected);
        }
    }

    private void addUser(String userId){
        try {
            Long chatId = server.createChat(this.loggedInUser.getUserName(), userId);

            ChatUserBox pair = createProfileBox(userId, chatId);

            chats.getChildren().add(pair);
        } catch(HTTPException e){
            e.printStackTrace();
        }
    }


}
