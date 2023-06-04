package server.commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Message {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user_1"),
            @JoinColumn(name = "user_2")
        }
    )
    private Chat chat;


    private String message;

    public Message(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }


    @Override
    public String toString(){
        return "Message = {" + message + "}";
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
