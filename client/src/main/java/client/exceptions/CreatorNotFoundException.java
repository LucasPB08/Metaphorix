package client.exceptions;

public class CreatorNotFoundException extends Exception{
    /**
     * Constructor
     * @param str Error message.
     */
    public CreatorNotFoundException(String str){
        super(str);
    }
}
