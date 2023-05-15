package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.commons.ChatUser;

import static org.junit.jupiter.api.Assertions.*;


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

}
