package client.utils;

import javafx.scene.layout.VBox;

public class ChatUserBox extends VBox {
    private Long chatId;

    public ChatUserBox(Long id){
        super(id);
    }

    public Long getChatId(){
        return chatId;
    }

}
