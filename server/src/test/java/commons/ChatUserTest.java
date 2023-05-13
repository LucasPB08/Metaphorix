package commons;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.commons.ChatUser;

import static org.junit.jupiter.api.Assertions.*;


public class ChatUserTest {
    private ChatUser user;

    @BeforeEach
    void setup(){
        this.user = new ChatUser("Lucas");
    }

    @Test
    void constructorTest(){
        assertNotNull(user);
    }

    @Test
    void setNameTest(){
        user.setName("User");
        assertEquals("User", user.getName());
    }

    @Test
    void getNameTest(){
        assertEquals("Lucas", user.getName());
    }

}
