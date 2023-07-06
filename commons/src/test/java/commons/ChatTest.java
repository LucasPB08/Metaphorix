package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ChatTest {
    private ChatUser initiator;
    private ChatUser receiver;
    private Chat sut;

    @BeforeEach
    void setup(){
        this.initiator = new ChatUser("Initiator", "HisName", "123");
        this.receiver = new ChatUser("Receiver", "HerName", "Password");
        this.sut = new Chat(initiator, receiver);
    }

    @Test
    void constructorTest(){
        Chat chat = new Chat();
        assertThat(chat).isNotNull();
        assertThat(sut).isNotNull();
    }

    @Test
    void gettersTest(){
        assertThat(sut.getInitiator()).isEqualTo(initiator);
        assertThat(sut.getReceiver()).isEqualTo(receiver);
    }

    @Test
    void setterTest(){
        ChatUser newInitiator = new ChatUser("New Initiator", "name", "pass");
        ChatUser newReceiver = new ChatUser("New Receiver", "their name", "345");

        sut.setInitiator(newInitiator);
        sut.setReceiver(newReceiver);

        assertThat(sut.getReceiver()).isEqualTo(newReceiver);
        assertThat(sut.getInitiator()).isEqualTo(newInitiator);
    }

    @Test
    void getMessagesTest(){
        Message message1 = new Message("hi");
        Message message2 = new Message("hello");
        Message message3 = new Message("hey");

        sut.addMessage(message1);
        sut.addMessage(message2);
        sut.addMessage(message3);

        assertThat(sut.getMessages()).containsOnly(message1,message2,message3);
    }

    @Test
    void addMessagesTest(){
        Message message1 = new Message("hi");
        Message message2 = new Message("hello");
        Message message3 = new Message("hey");

        sut.addMessage(message1);
        sut.addMessage(message2);
        sut.addMessage(message3);

        assertThat(sut.getMessages().get(0)).isEqualTo(message3);
    }
}
