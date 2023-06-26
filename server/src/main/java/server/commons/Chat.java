package server.commons;

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

    public Chat(){
        //for object mapper
    }

    public Chat(ChatUser initiator, ChatUser receiver){
        this.initiator = initiator;
        this.receiver = receiver;
        this.messages = new LinkedList<>();
    }

    public Long getId(){
        return this.id;
    }

    public ChatUser getInitiator() {
        return initiator;
    }

    public ChatUser getReceiver() {
        return receiver;
    }

    public void setInitiator(ChatUser initiator) {
        this.initiator = initiator;
    }

    public void setReceiver(ChatUser receiver) {
        this.receiver = receiver;
    }

    public void addMessage(Message message){
        this.messages.add(0, message);
    }

    public List<Message> getMessages(){
        return this.messages;
    }

    @Override
    public String toString(){
        return "Chat{" +
                "Initiator = " + initiator.toString() + ", \n" +
                "Receiver = " + receiver.toString() + "}";
    }

    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
