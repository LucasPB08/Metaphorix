package client.utils;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.commons.ChatUser;

import java.util.List;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080";
    private static final Configuration defaultConfiguration = ClientBuilder.newClient()
                                                                .getConfiguration();

    /**
     * Stores a user in the database
     * @param name name of user
     * @return Response type
     */
    public Response storeUser(String name){
        return ClientBuilder.newClient(defaultConfiguration).target(SERVER)
                .path(SERVER + "/" + name)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }

    /**
     * Gets all users in the database
     * @return List of all users in the database
     */
    public List<ChatUser> getUsers(){
        return ClientBuilder.newClient(defaultConfiguration)
                .target(SERVER).path("/user/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(new MyGenericType());
    }

}
