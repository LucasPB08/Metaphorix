package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Date;
import java.sql.Timestamp;

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

    private Timestamp timestampSent;

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
    public Message(String message, Timestamp timestampSent){
        this.message = message;
        this.timestampSent = timestampSent;
    }

    /**
     * Gets the message ID
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the chat that this message belongs to.
     * @return The Chat.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Gets the sender of this message.
     * @return The sender.
     */
    public ChatUser getSender() {
        return sender;
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
     * @return The string representation of this commons.Message.
     */
    @Override
    public String toString(){
        return "commons.Message = {" + message + "}";
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
