package api;

import api.fakes.ChatUserRepoFake;
import commons.Chat;
import commons.Message;
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

    @Test
    void getMessagesTest(){
        String id = "User";
        ChatUser user = new ChatUser(id, "Lucas PB", "123");

        Message mess1 = new Message("Message1");
        user.addMessage(mess1);

        Message mess2 = new Message("Message2");
        user.addMessage(mess2);

        fakeRepo.save(user);

        List<Message> exp = List.of(mess1, mess2);
        assertThat(sut.getMessages(id)).containsExactlyInAnyOrderElementsOf(exp);
    }

    @Test
    void getAllChatsTest1(){
        String id = "User";
        ChatUser user = new ChatUser(id, "Lucas PB", "123");

        fakeRepo.save(user);

        assertThat(sut.getChats(id)).isEmpty();
    }

    @Test
    void getAllChatsTest2(){
        String id = "User";
        ChatUser user = new ChatUser(id, "Lucas PB", "123");

        ChatUser user2 = new ChatUser("user2", "mike", "1234");

        Chat c1 = new Chat(user, user2);
        user.addInitiatedChat(c1);
        user2.addReceivedChat(c1);

        fakeRepo.save(user);

        assertThat(sut.getChats(id)).contains(c1);
    }

    @Test
    void getAllChatsTest3(){
        String id = "User";
        ChatUser user = new ChatUser(id, "Lucas PB", "123");
        ChatUser user2 = new ChatUser("user2", "mike", "1234");
        ChatUser user3 = new ChatUser("user3", null, null);

        Chat c1 = new Chat(user, user2);
        Chat c2 = new Chat(user, user3);

        user.addInitiatedChat(c1);
        user.addInitiatedChat(c2);

        user2.addReceivedChat(c1);
        user3.addReceivedChat(c2);

        fakeRepo.save(user);

        assertThat(sut.getChats(id)).contains(c1, c2);
    }

    @Test
    void getAllChatsTest4(){
        String id = "User";
        ChatUser user = new ChatUser(id, "Lucas PB", "123");
        ChatUser user2 = new ChatUser("user2", "mike", "1234");
        ChatUser user3 = new ChatUser("user3", null, null);

        Chat c1 = new Chat(user2, user);

        Chat c2 = new Chat(user3, user);

        user.addReceivedChat(c1);
        user.addReceivedChat(c2);

        user2.addInitiatedChat(c1);
        user3.addInitiatedChat(c2);

        fakeRepo.save(user);

        assertThat(sut.getChats(id)).contains(c1, c2);
    }

    @Test
    void getMessagesUserNotSavedTest(){
        String id = "User";
        ChatUser user = new ChatUser(id, "MyName", "password");

        Message message = new Message("Content");
        user.addMessage(message);

        assertThat(sut.getMessages(id)).isNull();
    }

    @Test
    void getChatsUserNotSavedTest(){
        String id = "User";
        ChatUser user = new ChatUser(id, "MyName", "password");
        ChatUser user2 = new ChatUser("Username", "HisName", "123");

        Chat chat = new Chat(user, user2);
        user.addInitiatedChat(chat);
        user2.addReceivedChat(chat);

        assertThat(sut.getChats(id)).isNull();
    }

}
