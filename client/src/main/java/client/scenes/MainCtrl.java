package client.scenes;

import client.utils.ChatBox;
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import commons.ChatUser;

import java.util.List;

@Singleton
public class MainCtrl {
    private Stage primaryStage;

    private SignInCtrl signInCtrl;
    private Scene signInScene;

    private SignUpCtrl signUpCtrl;
    private Scene signUpScene;

    private ChatOverviewController chatOverviewController;
    private Scene chatOverviewScene;

    private UserOverviewController userOverviewController;
    private Scene userOverviewScene;

    private GroupChatCreationController groupCreationController;
    private Scene groupCreationScene;

    /**
     * Initialises controller
     * @param stage primary stage
     * @param signIn signIn pair
     * @param signUp signUp pair
     * @param chatOverview chat overview pair
     * @param userOverview user overview pair
     */
    public void init(Stage stage, Pair<SignInCtrl, Scene> signIn,
                     Pair<SignUpCtrl, Scene> signUp,
                     Pair<ChatOverviewController, Scene> chatOverview,
                     Pair<UserOverviewController, Scene> userOverview,
                     Pair<GroupChatCreationController, Scene> groupChatOverview){
        this.primaryStage = stage;

        this.signInCtrl = signIn.getKey();
        this.signInScene = signIn.getValue();

        this.signUpCtrl = signUp.getKey();
        this.signUpScene = signUp.getValue();

        this.chatOverviewController = chatOverview.getKey();
        this.chatOverviewScene = chatOverview.getValue();

        this.userOverviewController = userOverview.getKey();
        this.userOverviewScene = userOverview.getValue();

        this.groupCreationController = groupChatOverview.getKey();
        this.groupCreationScene = groupChatOverview.getValue();

        showSignIn();
        stage.show();
    }

    /**
     * Shows sign in scene.
     */
    public void showSignIn(){
        primaryStage.setTitle("Sign in");
        primaryStage.setScene(signInScene);
    }

    /**
     * Shows sign up scene.
     */
    public void showSignUp(){
        primaryStage.setTitle("Sign up");
        primaryStage.setScene(signUpScene);
    }

    /**
     * Shows the user overview
     */
    public void showUserOverview(){
        userOverviewController.loadProfile();
        userOverviewController.loadChats();

        primaryStage.setTitle("User");
        primaryStage.setScene(userOverviewScene);
    }

    /**
     * Sets the logged-in user field in the user and chat overview controller.
     * @param loggedInUser The logged-in user.
     */
    public void login(ChatUser loggedInUser){
        userOverviewController.setLoggedInUser(loggedInUser);
        chatOverviewController.setLoggedInUser(loggedInUser);
    }

    /**
     * Processes the click on a profile box when in the user overview.
     * @param profileBox
     */
    public void clickOnChat(ChatBox profileBox){
        this.chatOverviewController.sync();
        this.chatOverviewController.clickOnChat(profileBox);

        primaryStage.setTitle("Chats");
        primaryStage.setScene(chatOverviewScene);
    }

    /**
     * Gets the list of the names of which the logged-in user has an active chat with.
     * @return the list.
     */
    public List<String> getListOfChattingUsers(){
        return userOverviewController.getNamesOfChatters();
    }

    public void showGroupCreation(ChatUser creator){
        groupCreationController.setup(creator);

        primaryStage.setTitle("Group Chat Creation");
        primaryStage.setScene(groupCreationScene);
    }
}
