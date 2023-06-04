package server.commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;


@Entity
public class Chat {

    @EmbeddedId
    private ChatKey id;

    @ManyToOne
    @MapsId("user_1")
    @JoinColumn(name="initiator_id")
    private ChatUser initiator;

    @ManyToOne
    @MapsId("user_2")
    @JoinColumn(name="receiver_id")
    private ChatUser receiver;

//    private Map<ChatUser, List<Message>> messages;

    public Chat(){
        //for object mapper
    }

    public Chat(ChatKey id, ChatUser initiator, ChatUser receiver){
        this.id = id;
        this.initiator = initiator;
        this.receiver = receiver;
        //messages = new HashMap<>();
    }

    public ChatKey getId() {
        return id;
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
