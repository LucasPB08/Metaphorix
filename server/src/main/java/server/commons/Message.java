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

    private String message;

    public Message(){
        //for object mapper
    }

    public Message(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setChat(Chat chat){
        this.chat = chat;
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
