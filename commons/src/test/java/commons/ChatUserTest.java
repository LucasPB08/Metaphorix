package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class ChatUserTest {
    private ChatUser user;

    @BeforeEach
    void setup(){
        this.user = new ChatUser("UserName", "Lucas", "123");
    }

    @Test
    void constructorTest(){
        assertNotNull(user);
    }

    @Test
    void setUserNameTest(){
        user.setUserName("User");
        assertEquals("User", user.getUserName());
    }

    @Test
    void getUserNameTest(){
        assertEquals("UserName", user.getUserName());
    }

    @Test
    void getFullNameTest(){
        assertEquals("Lucas", user.getFullName());
    }

    @Test
    void validatePasswordTest(){
        assertFalse(user.validatePassword("124"));
    }

    @Test
    void validateCorrectPassword(){
        assertTrue(user.validatePassword("123"));
    }

    @Test
    void getMessagesTest(){
        Message message1 = new Message("content");
        Message message2 = new Message("content2");

        user.addMessage(message1);
        user.addMessage(message2);

        assertThat(user.getMessages()).containsOnly(message1, message2);
    }

    @Test
    void allChatsTest(){
        ChatUser user2 = new ChatUser("user2", "HerName", "password");
        ChatUser user3 = new ChatUser("user3", "HisName" , "123");

        Chat chat = new Chat(user, user2);
        user.addInitiatedChat(chat);

        Chat chat2 = new Chat(user3, user);
        user.addReceivedChat(chat2);

        assertThat(user.allChats()).containsOnly(chat, chat2);
    }



}
