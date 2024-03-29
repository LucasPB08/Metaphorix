package client.utils;

import client.exceptions.CreatorNotFoundException;
import client.exceptions.EntityAlreadyExistsException;
import client.exceptions.EntityNotFoundException;
import client.exceptions.HTTPException;
import client.generics.*;
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
            throws HTTPException, EntityAlreadyExistsException {
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
                .get(new ListOfUsersGenericType());
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

    /**
     * Sends a HTTP post to create a new group chat.
     * @param creatorId The username of the creation
     * @param groupName The name of the group
     * @param groupDesc The description of the group, may be empty
     * @param otherParticipants The additional participants of the group,
     *                          may be empty.
     * @throws CreatorNotFoundException If the creator was not found in the database.
     */
    public void createGroupChat(String creatorId,
                                String groupName,
                                String groupDesc,
                                List<String> otherParticipants) throws CreatorNotFoundException {
        WebTarget target = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/create").queryParam("creatorId", creatorId)
                .queryParam("groupName", groupName);

        for(String participant: otherParticipants)
            target = target.queryParam("addedUsersIds", participant);

        Response response = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(groupDesc));

        if(response.getStatus() != OK_STATUS)
            throw new CreatorNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);
    }

    /**
     * Sends an HTTP GET request to retrieve all the group chats
     * a certain user is part of.
     * @param userName The username
     * @return All the group chats.
     */
    public List<GroupChatDTO> getGroupChatsOfUser(String userName){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/users/groups")
                .queryParam("userId", userName)
                .request()
                .get(new ListOfGroupGenericType());
    }

    /**
     * Sends an HTTP GET request to retrieve all the messages
     * sent to a group chat.
     * @param groupId The id of the group chat
     * @return All the messages sent.
     */
    public List<GroupMessage> getGroupChatMessages(Long groupId){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/messages").queryParam("groupId", groupId)
                .request()
                .get(new ListOfGroupMessagesGenericType());
    }

    /**
     * Sends an HTTP GET request to retrieve a group chat.
     * @param chatId The id of the group chat
     * @return The group chat.
     */
    public GroupChat getGroupChatById(Long chatId){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/chat").queryParam("chatId", chatId)
                .request()
                .get(GroupChat.class);
    }

    /**
     * Sends an HTTP POST request to save a message sent to a group chat.
     * @param groupChatId The id of the group chat.
     * @param userName The username of the sender.
     * @param message The content of the message.
     * @return The saved group message.
     * @throws EntityNotFoundException If the group chat or user was not found
     * in the database.
     */
    public GroupMessage sendGroupMessage(Long groupChatId,
                                 String userName,
                                 String message) throws EntityNotFoundException {
        Response response = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/messages").queryParam("groupId", groupChatId)
                .queryParam("senderId", userName)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(message));

        if(response.getStatus() != OK_STATUS)
            throw new EntityNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);

        return response.readEntity(GroupMessage.class);
    }

    /**
     * Sends an HTTP PUT request to edit the description of
     * a group chat.
     * @param chatId The id of the group chat.
     * @param newDescription The new description
     * @throws EntityNotFoundException If the group chat could not be found
     * in the database.
     */
    public void editGroupDescription(Long chatId,
                                     String newDescription) throws EntityNotFoundException {
        Response response = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/description")
                .queryParam("chatId", chatId)
                .request()
                .put(Entity.json(newDescription));

        if(response.getStatus() != OK_STATUS)
            throw new EntityNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);
    }

    /**
     * Sends an HTTP PUT request to remove a user from the group.
     * @param groupId The id of the group.
     * @param userName The username of the user to be removed.
     * @throws EntityNotFoundException If the user could not be found
     *  in the database.
     */
    public void removeUserFromGroup(Long groupId, String userName) throws EntityNotFoundException {
        Response response = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/remove-participant").queryParam("chatId", groupId)
                .request()
                .put(Entity.json(userName));

        if(response.getStatus() != OK_STATUS)
            throw new EntityNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);
    }

    /**
     * Sends an HTTP DELETE request to delete a group chat
     * @param groupId The id of the group chat
     * @throws EntityNotFoundException if the group chat could not be found
     *  in the database.
     */
    public void deleteGroupChat(Long groupId) throws EntityNotFoundException {
        Response response = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/chat")
                .queryParam("chatId", groupId)
                .request()
                .delete();

        if(response.getStatus() != OK_STATUS)
            throw new EntityNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);
    }

    /**
     * Sends an HTTP POST request to add users to a group chat.
     * @param groupId The id of the group chat.
     * @param usersToAdd The usernames of the users to add.
     * @return The group chat
     * @throws EntityNotFoundException If the group chat could not be found.
     */
    public GroupChat addParticipantsToGroup(Long groupId,
                                            List<String> usersToAdd) throws EntityNotFoundException{
        WebTarget target = ClientBuilder.newClient(new ClientConfig()).target(SERVER)
                .path("/groups/participants").queryParam("chatId", groupId);

        for(String user: usersToAdd)
            target = target.queryParam("userIds", user);

        Response response = target.request().post(null);

        if(response.getStatus() != OK_STATUS)
            throw new EntityNotFoundException("HTTP STATUS: " + response.getStatus());

        System.out.println(response);

        return response.readEntity(GroupChat.class);
    }
}
