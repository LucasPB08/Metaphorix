package client.scenes;

import client.MyApplication;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import server.commons.ChatUser;


public class UserOverviewController{
    private MainCtrl mainCtrl;
    private ServerUtils server;

    private ChatUser user;

    private int profilePictureRadius = 45;

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
        userSection.getChildren().add(createProfileBox(user));
    }

    private VBox createProfileBox(ChatUser user){
        VBox profileBox = new VBox();

        Circle profilePicture = new Circle();
        profilePicture.setRadius(profilePictureRadius);
        profilePicture.setFill(loadImage());

        profileBox.getChildren().addAll(profilePicture, new Text(user.getUserName()));

        return profileBox;
    }

    private ImagePattern loadImage(){
        Image pic = new Image("C:\\Users\\Lucas\\Documents\\Personal\\Code\\Projects\\Java\\Metaphorix\\client\\src\\main\\resources\\images\\profile-pic.jpg");
        ImagePattern imagePattern = new ImagePattern(pic);

        return imagePattern;
    }

    public void addChat(){
        Text text = new Text();
        text.setText(user.getUserName());

        Image pic = new Image("C:\\Users\\Lucas\\Documents\\Personal\\Code\\Projects\\Java\\Metaphorix\\client\\src\\main\\resources\\images\\profile-pic.jpg");

        Circle circle = new Circle();
        circle.setRadius(profilePictureRadius);
        circle.setFill(new ImagePattern(pic));

        VBox pair = new VBox();
        pair.getChildren().addAll(circle, text);

        chats.getChildren().add(pair);
    }

}
