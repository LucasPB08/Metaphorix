package client.utils;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import server.commons.ChatUser;

import java.util.List;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080";
    @SuppressWarnings("checkstyle:StaticVariableName")
    private static int OK_STATUS = 200;

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
                .get(new MyGenericType());
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

}
