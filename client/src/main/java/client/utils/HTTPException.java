package client.utils;

public class HTTPException extends Exception{
    /**
     * Constructor for exception
     * @param message message of exception
     */
    public HTTPException(String message){
        super(message);
    }
}
