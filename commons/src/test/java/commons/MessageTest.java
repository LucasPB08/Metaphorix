package commons;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MessageTest {

    @Test
    void constructorTest(){
        Message message1 = new Message("Content");
        Message message2 = new Message();

        assertThat(message2).isNotNull();
        assertThat(message1).isNotNull();
    }

    @Test
    void gettersAndSettersTest(){
        String text = "My message";
        Message message =new Message(text);

        ChatUser sender = new ChatUser("Sender", "Lucas", "password");
        ChatUser user2 = new ChatUser("Receivr", "Mike", "password");
        Chat chat = new Chat(sender, user2);

        message.setChat(chat);
        message.setSender(sender);

        assertThat(message.getChat()).isEqualTo(chat);
        assertThat(message.getSender()).isEqualTo(sender);
        assertThat(message.getMessage()).isEqualTo(text);
    }

}
