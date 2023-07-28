package client.utils;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ChatBox extends VBox {
    private final Long chatId;
    private final boolean isGroupChat;

    /**
     * Constructor for a ChatUserBox
     * @param id id of the commons.ChatUser stored in this box
     */
    public ChatBox(Long id){
        this.chatId = id;
        this.getStyleClass().add("user-box");
        this.setAlignment(Pos.CENTER);
        this.isGroupChat = false;
    }

    /**
     * Constructor
     * @param id The id of the chat
     * @param isGroupChat Whether the box is used to display a group chat.
     */
    public ChatBox(Long id, boolean isGroupChat){
        this.chatId = id;
        this.getStyleClass().add("user-box");
        this.setAlignment(Pos.CENTER);
        this.isGroupChat = isGroupChat;
    }

    /**
     * Gets id of the stored user
     * @return id of the stored user
     */
    public Long getChatId(){
        return chatId;
    }

    /**
     * Whether this box is used for a group chat
     * @return True if this is used for a group chat,
     * false otherwise.
     */
    public boolean isGroupChat(){
        return isGroupChat;
    }

}
