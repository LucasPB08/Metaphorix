package client.scenes;

import client.MyApplication;
import client.utils.ChatUserBox;
import client.utils.HTTPException;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;
import server.commons.ChatUser;

import java.util.Optional;


public class UserOverviewController{
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private ChatUser user;

    private int profilePictureRadius = 45;

    private ChatUserBox selectedUser;

    @FXML
    private TextField messageBox;

    @FXML
    private HBox chats;

    @FXML
    private Pane userSection;

    @FXML
    public void initialize(){
        mainCtrl = MyApplication.getMainCtrl();
        server = MyApplication.getServer();
    }

    public void setUser(ChatUser user){
        this.user = user;
    }

    public void loadProfile(){
        userSection.getChildren().add(createProfileBox(user.getUserName(), -1L));
    }

    public void addChat(){
        Pair<AddChatsCtrl, Dialog<ButtonType>> pair = makeDialog();
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

    public void sendMessage(){
        try {
            String message = messageBox.getText();

            server.sendMessage(selectedUser.getChatId(),user.getUserName() , message);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Pair<AddChatsCtrl ,Dialog<ButtonType>> makeDialog(){
        try {
            FXMLLoader loader = new FXMLLoader(MyApplication.class.
                    getResource("scenes/add-user-dialog.fxml"));

            DialogPane pane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);

            return new Pair<>(loader.getController(), dialog);
        } catch(Exception e){
            System.err.println("Something went wrong");
            e.printStackTrace();
            return null;
        }
    }

    private void addUser(String userId){
        try {
            Long chatId = server.createChat(this.user.getUserName(), userId);

            ChatUserBox pair = createProfileBox(userId, chatId);
            makeSelectable(pair);

            chats.getChildren().add(pair);
        } catch(HTTPException e){
            e.printStackTrace();
        }
    }

    private ChatUserBox createProfileBox(String user, Long chatId){
        ChatUserBox profileBox = new ChatUserBox(chatId);

        Circle profilePicture = new Circle();
        profilePicture.setRadius(profilePictureRadius);
        profilePicture.setFill(loadImage());

        profileBox.getChildren().addAll(profilePicture, new Text(user));

        return profileBox;
    }

    private ImagePattern loadImage(){
        Image pic = new Image("images/profile-pic.jpg");

        return new ImagePattern(pic);
    }

    private void makeSelectable(ChatUserBox profileBox) {
        profileBox.setOnMouseClicked(event -> {
            if(selectedUser != null) selectedUser.setStyle("-fx-background-color: null;");

            this.selectedUser = profileBox;
            selectedUser.setStyle("-fx-background-color: blue;");
        });
    }
}
