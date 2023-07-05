package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ChatUser initiator;

    @ManyToOne
    private ChatUser receiver;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    /**
     * Default constructor for object mapper
     */
    public Chat(){
        //for object mapper
    }

    /**
     * Constructor for commons.Chat object
     * @param initiator user that initiated the chat
     * @param receiver user at the receiving end of a chat
     */
    public Chat(ChatUser initiator, ChatUser receiver){
        this.initiator = initiator;
        this.receiver = receiver;
        this.messages = new LinkedList<>();
    }

    /**
     * Getter for chat id
     * @return chat id
     */
    public Long getId(){
        return this.id;
    }

    /**
     * Getter for the initiator of the chat
     * @return initiator of the chat
     */
    public ChatUser getInitiator() {
        return initiator;
    }

    /**
     * Getter for the receiver of the chat
     * @return receiver of the chat
     */
    public ChatUser getReceiver() {
        return receiver;
    }

    /**
     * Sets the initiator of the chat
     * @param initiator initiator of the chat
     */
    public void setInitiator(ChatUser initiator) {
        this.initiator = initiator;
    }

    /**
     * Sets the receiver of the chat
     * @param receiver receiver of the chat
     */
    public void setReceiver(ChatUser receiver) {
        this.receiver = receiver;
    }

    /**
     * Adds a message to the chat
     * @param message message sent
     */
    public void addMessage(Message message){
        this.messages.add(0, message);
    }

    /**
     * Getter for the messages of the chat
     * @return all messages sent in this chat
     */
    public List<Message> getMessages(){
        return this.messages;
    }

    /**
     * Makes this chat into a string representation
     * @return A string representation of this chat
     */
    @Override
    public String toString(){
        return "commons.Chat{" +
                "Initiator = " + initiator.toString() + ", \n" +
                "Receiver = " + receiver.toString() + "}";
    }

    /**
     * Checks for equality between this chat and an object
     * @param o other object
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Creates a hash code from this object
     * @return integer that is the hash code of this object
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
