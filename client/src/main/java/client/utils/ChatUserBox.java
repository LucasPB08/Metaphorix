package client.utils;

import javafx.scene.layout.VBox;

public class ChatUserBox extends VBox {
    private Long chatId;

    /**
     * Constructor for a ChatUserBox
     * @param id id of the commons.ChatUser stored in this box
     */
    public ChatUserBox(Long id){
        this.chatId = id;
    }

    /**
     * Gets id of the stored user
     * @return id of the stored user
     */
    public Long getChatId(){
        return chatId;
    }

}
