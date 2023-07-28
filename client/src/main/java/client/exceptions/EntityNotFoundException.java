package client.exceptions;

public class EntityNotFoundException extends Exception{

    /**
     * Constructor
     * @param message Error message
     */
    public EntityNotFoundException(String message){
        super(message);
    }
}
