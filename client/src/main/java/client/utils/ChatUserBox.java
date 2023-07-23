package client.utils;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ChatUserBox extends VBox {
    private final Long chatId;
    private final boolean isGroupChat;

    /**
     * Constructor for a ChatUserBox
     * @param id id of the commons.ChatUser stored in this box
     */
    public ChatUserBox(Long id){
        this.chatId = id;
        this.getStyleClass().add("user-box");
        this.setAlignment(Pos.CENTER);
        this.isGroupChat = false;
    }

    public ChatUserBox(Long id, boolean isGroupChat){
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

    public boolean isGroupChat(){
        return isGroupChat;
    }

}
