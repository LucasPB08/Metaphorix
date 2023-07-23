package client.utils;

import client.generics.ListOfChatUserGenericType;
import client.generics.ListOfChatsGenericType;
import client.generics.ListOfGroupsGenericType;
import client.generics.ListOfMessagesGenericType;
import com.google.inject.Singleton;
import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

@Singleton
public class ServerUtils {
    private static final String SERVER = "http://localhost:8080";
    private StompSession session = connect("ws://localhost:8080/websocket");
    @SuppressWarnings("checkstyle:StaticVariableName")
    final private static int OK_STATUS = 200;

    private StompSession connect(String url){
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stomp = new WebSocketStompClient(client);

        stomp.setMessageConverter(new MappingJackson2MessageConverter());

        try {
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch(Exception e){
            e.printStackTrace();
        }

        throw new IllegalStateException();
    }

    /**
     * Register for websocket messages.
     * @param dest Destination to listen to.
     * @param type The class type of the message sent.
     * @param consumer Consumer to execute on the message.
     * @param <T> Type of message.
     */
    public <T> void registerForWebsocketMessages(String dest, Class<T> type, Consumer<T> consumer){
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept( (T) payload);
            }
        });
    }

    /**
     * Sends a websocket message.
     * @param dest destination to send message to.
     * @param o Object to send.
     */
    public void send(String dest, Object o){
        session.send(dest, o);
    }

    /**
     * Stores a new user in the database
     * @param fullName full name of user
     * @param userName username
     * @param password password of user
     */
    public void storeUser(String fullName, String userName, String password)
            throws HTTPException, EntityAlreadyExistsException{
        if(existsUser(userName)) throw new EntityAlreadyExistsException("This user already exists");

        Response response = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/users/store")
                .queryParam("fullName", fullName)
                .queryParam("userName", userName)
                .queryParam("password", password)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(null));

        if(response.getStatus() != OK_STATUS) {
            throw new HTTPException("HTTP status: " + response.getStatus());
        }

        System.out.println(response);
    }

    /**
     * Gets all users in the database
     * @return List of all users in the database
     */
    public List<ChatUser> getUsers(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/users/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new ListOfChatUserGenericType());
    }

    /**
     * Gets user by username
     * @param name username
     * @return user
     */
    public ChatUser getUserById(String name){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/users/user")
                .queryParam("id", name)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ChatUser.class);
    }

    /**
     * Checks if the user exists
     * @param id username to check
     * @return true if user exists, false otherwise.
     */
    public Boolean existsUser(String id){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/users/exists")
                .queryParam("id", id)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Boolean.class);
    }

    /**
     * Checks if the password matches with the user's password
     * @param name username
     * @param password password
     * @return true if passwords match, false otherwise.
     */
    public Boolean validatePassword(String name, String password){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/users/password")
                .queryParam("user", name)
                .queryParam("password", password)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Boolean.class);
    }

    /**
     * Creates a new chat in the database.
     * @param initiatorId Id of the initiator of the chat.
     * @param receiverId Id of the receiver of the chat.
     * @return The Id of the created chat
     * @throws HTTPException If the response status is not OK
     */
    public Long createChat(String initiatorId, String receiverId) throws HTTPException {
        Response response =  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/chat")
                .queryParam("initiatorId", initiatorId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(receiverId));

        if(response.getStatus() != OK_STATUS)
            throw new HTTPException("HTTP Status: " + response.getStatus());

        System.out.println(response);

        Chat savedChat = response.readEntity(Chat.class);

        return savedChat.getId();
    }

    /**
     * Gets all the chats a certain user participates in.
     * @param userId Id of the user.
     * @return all the chats this user participates in.
     */
    public List<Chat> getChatsOfUser(String userId){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/users/chats").queryParam("userId", userId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new ListOfChatsGenericType());
    }

    /**
     * Sends a message.
     * @param chatId Id of the chat where the message is sent.
     * @param userId Id of the sender.
     * @param message Content of the message
     * @throws HTTPException If the response status is not OK.
     * @return message saved to the database.
     */
    public Message sendMessage(Long chatId, String userId, String message) throws HTTPException{
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("/chat").queryParam("chatId", chatId)
                .queryParam("userId", userId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(message));

        if(response.getStatus() != OK_STATUS)
            throw new HTTPException("HTTP Status: " + response.getStatus());

        return response.readEntity(Message.class);
    }

    /**
     * Gets the messages of a certain chat.
     * @param chatId The id of the chat to get the messages from.
     * @return List of the chat's messages.
     */
    public List<Message> getMessagesOfChat(Long chatId){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/chat").queryParam("chatId", chatId)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new ListOfMessagesGenericType());
    }

    public void createGroupChat(String creatorId, String groupName, String groupDesc, List<String> otherParticipants) throws CreatorNotFoundException {
        WebTarget target = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/create").queryParam("creatorId", creatorId)
                .queryParam("groupName", groupName);

        for(String participant: otherParticipants)
            target.queryParam("addedUsersIds", participant);

        Response response = target.request().accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(groupDesc));

        if(response.getStatus() != OK_STATUS) throw new CreatorNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);
    }

    public List<GroupChatDTO> getGroupChatsOfUser(String userName){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/users/groups")
                .queryParam("userId", userName)
                .request()
                .get(new ListOfGroupsGenericType());
    }
}
