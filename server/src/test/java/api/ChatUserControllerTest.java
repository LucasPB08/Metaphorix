package api;

import api.fakes.ChatUserRepoFake;
import org.junit.jupiter.api.Test;
import server.api.ChatUserController;
import commons.ChatUser;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ChatUserControllerTest {
    private ChatUserRepoFake fakeRepo = new ChatUserRepoFake();
    private ChatUserController sut = new ChatUserController(fakeRepo);

    @Test
    void storeUserTest(){
        String name = "User";
        String userName = "UserName";
        String password = "123";

        sut.storeUser(name, userName, password);
        ChatUser user = new ChatUser(userName,name,password);

        List<ChatUser> users = fakeRepo.getUsers();
        assertThat(users).contains(user);
    }

    @Test
    void existsTest(){
        ChatUser user = new ChatUser("Username", "Lucas", "123");
        ChatUser user2 = new ChatUser("Userpb", "Mike", "password");
        List<ChatUser> users = Arrays.asList(user2);
        fakeRepo.users(users);

        assertThat(sut.existsUser(user.getUserName())).isFalse();
        assertThat(sut.existsUser(user2.getUserName())).isTrue();
    }

    @Test
    void getUserTest(){
        ChatUser user = new ChatUser("Username", "Lucas", "123");
        ChatUser user2 = new ChatUser("Userpb", "Mike", "password");
        List<ChatUser> users = Arrays.asList(user2);
        fakeRepo.users(users);

        assertThat(sut.getUser("Userpb")).isEqualTo(user2);
        assertThat(sut.getUser("Username")).isNull();
    }

    @Test
    void validatePasswordTest(){
        String userName = "Userpb";
        String password = "password";

        ChatUser user2 = new ChatUser(userName, "Mike", password);

        List<ChatUser> users = Arrays.asList(user2);
        fakeRepo.users(users);

        assertThat(sut.validatePassword(userName, "123")).isFalse();
        assertThat(sut.validatePassword(userName, password)).isTrue();
        assertThat(sut.validatePassword("Doesn't exist", "123")).isFalse();
    }

    @Test
    void getAllTest(){
        ChatUser user = new ChatUser("Username", "Lucas", "123");
        ChatUser user2 = new ChatUser("Userpb", "Mike", "password");
        List<ChatUser> users = Arrays.asList(user2);
        fakeRepo.users(users);

        assertThat(sut.getAll()).isEqualTo(users);

    }


}
