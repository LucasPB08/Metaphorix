package client.utils;

public class EntityAlreadyExistsException extends Exception{
    public EntityAlreadyExistsException(String message){
        super(message);
    }
}
