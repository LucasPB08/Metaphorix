package server.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Embeddable
public class ChatKey implements Serializable {

    @Column(name = "user_1")
    String initiator;

    @Column(name = "user_2")
    String receiver;

    public ChatKey(){
        //for object mapper
    }

    public ChatKey(String initiator, String receiver) {
        this.initiator = initiator;
        this.receiver = receiver;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getReceiver(){
        return receiver;
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object o){
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
