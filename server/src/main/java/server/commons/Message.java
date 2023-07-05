package server.commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ChatUser sender;

    private String message;

    /**
     * Default constructor for object mapper
     */
    public Message(){
        //for object mapper
    }

    /**
     * Constructor for message
     * @param message String context of the message
     */
    public Message(String message){
        this.message = message;
    }

    /**
     * Gets the content of this message
     * @return content of the message
     */
    public String getMessage(){
        return message;
    }

    /**
     * Sets the foreign key constraint of chat_id
     * @param chat The chat this message references. (This message is sent in that chat)
     */
    public void setChat(Chat chat){
        this.chat = chat;
    }

    /**
     * Sets the foreign key constraint of user_id.
     * @param sender The sender of this message
     */
    public void setSender(ChatUser sender){
        this.sender = sender;
    }

    /**
     * Makes a string representation of this message.
     * @return The string representation of this Message.
     */
    @Override
    public String toString(){
        return "Message = {" + message + "}";
    }

    /**
     * Checks equality between this message and another object.
     * @param o The object to check equality against this message
     * @return True if this equals other, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Hashes this message
     * @return integer hash.
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
