package client.utils;

public class EntityAlreadyExistsException extends Exception{
    /**
     * Constructor for exception
     * @param message exception message
     */
    public EntityAlreadyExistsException(String message){
        super(message);
    }
}
