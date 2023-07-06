package api;

import commons.Chat;
import commons.ChatUser;
import commons.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.MessagingAdviceBean;
import server.api.ChatController;
import server.database.ChatRepository;
import server.database.ChatUserRepository;
import server.database.MessageRepo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatControllerTest {

    private ChatRepository chatRepository = mock(ChatRepository.class);

    private ChatUserRepository chatUserRepository = mock(ChatUserRepository.class);

    private MessageRepo messageRepo = mock(MessageRepo.class);

    private ChatController sut = new ChatController(chatRepository, chatUserRepository, messageRepo);

    private ChatUser user1;
    private ChatUser user2;

    @BeforeEach
    void setup(){
        user1 = new ChatUser("user1" , "HisName", "123");
        user2 = new ChatUser("user2", "HerName", "1234");
    }

    @Test
    void constructorTest(){
        Chat chat = new Chat(user1, user2);

        assertThat(chat).isNotNull();
    }

    @Test
    void createChatTest(){
        String user1Id = user1.getUserName();
        String user2Id = user2.getUserName();

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(chatUserRepository.findById(user2Id)).thenReturn(Optional.of(user2));

        Chat exp = new Chat(user1, user2);


        sut.createChat(user1Id, user2Id);


        verify(chatRepository).save(exp);
        verify(chatUserRepository).save(user1);
        verify(chatUserRepository).save(user2);
    }

    @Test
    void createChatWithUserNotSavedInDatabaseTest1(){
        String user1Id = user1.getUserName();
        String user2Id = user2.getUserName();

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.empty());
        when(chatUserRepository.findById(user2Id)).thenReturn(Optional.of(user2));

        assertThat(sut.createChat(user1Id, user2Id)).isEqualTo(ResponseEntity.badRequest().build());
    }

    @Test
    void createChatWithUserNotSavedInDatabaseTest2(){
        String user1Id = user1.getUserName();
        String user2Id = user2.getUserName();

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(chatUserRepository.findById(user2Id)).thenReturn(Optional.empty());

        assertThat(sut.createChat(user1Id, user2Id)).isEqualTo(ResponseEntity.badRequest().build());
    }

    @Test
    void sendMessage(){
        String user1Id = user1.getUserName();
        String messageText = "My message";

        Chat chat = new Chat(user1, user2);
        Long chatId = 42L; //arbitrary id for mocking

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        sut.saveMessage(chatId, user1Id, messageText);

        Message exp = new Message(messageText);
        exp.setSender(user1);
        exp.setChat(chat);

        verify(messageRepo).save(exp);
    }

    @Test
    void sendMessageWithInvalidUserTest(){
        String user1Id = user1.getUserName();
        String messageText = "My message";

        Chat chat = new Chat(user1, user2);
        Long chatId = 42L; //arbitrary id for mocking

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.empty());
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        assertThat(sut.saveMessage(chatId, user1Id, messageText)).isEqualTo(ResponseEntity.badRequest().build());
    }

    @Test
    void sendMessageWithInvalidChatTest(){
        String user1Id = user1.getUserName();
        String messageText = "My message";

        Chat chat = new Chat(user1, user2);
        Long chatId = 42L; //arbitrary id for mocking

        when(chatUserRepository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        assertThat(sut.saveMessage(chatId, user1Id, messageText)).isEqualTo(ResponseEntity.badRequest().build());
    }

    @Test
    void getMessagesEmptyChatTest(){
        Chat chat = new Chat(user1, user2);
        Long chatId = 42L;

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        assertThat(sut.getMessages(chatId)).isEmpty();
    }

    @Test
    void getMessages(){
        Chat chat = new Chat(user1, user2);
        Long chatId = 42L;

        Message message1 = new Message("message1");
        Message message2 = new Message("message2");

        chat.addMessage(message1);
        chat.addMessage(message2);

        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        assertThat(sut.getMessages(chatId)).containsOnly(message1, message2);
    }

    @Test
    void getMessagesInvalidChatTest() {
        Chat chat = new Chat(user1, user2);
        Long chatId = 42L;

        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        assertThat(sut.getMessages(chatId)).isNull();
    }



    }
