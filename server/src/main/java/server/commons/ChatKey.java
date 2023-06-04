package server.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Embeddable
public class ChatKey implements Serializable {

    @Column(name = "user_1")
    private String initiatorId;

    @Column(name = "user_2")
    private String receiverId;

    public ChatKey(){
        //for object mapper
    }

    public ChatKey(String initiatorId, String receiverId) {
        this.initiatorId = initiatorId;
        this.receiverId = receiverId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public String getReceiverId(){
        return receiverId;
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
