package client.scenes;

import client.FXMLBuilder;
import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.ChatUser;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserOverviewController extends OverviewParent{

    /**
     * Constructor
     * @param mainCtrl The main controller of the application
     * @param server The server to communicate with.
     */
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

    public void createNewGroupChat(){
        mainCtrl.showGroupCreation(this.loggedInUser);
    }

    /**
     * Gets the names of the users who have a chat with the logged-in user.
     * @return the list of names.
     */
    public List<String> getNamesOfChatters(){
        List<Node> chats = this.chats.getChildren();

        List<String> usersChatting = new ArrayList<>();

        for(Node n: chats){

            List<Node> children = ((ChatUserBox) n).getChildren();

            for(Node child: children){
                if(child.getClass() != Text.class) continue;

                String userNameInBox = ((Text)child).getText();

                usersChatting.add(userNameInBox);
            }
        }

        usersChatting.add(this.loggedInUser.getUserName());

        return usersChatting;
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
