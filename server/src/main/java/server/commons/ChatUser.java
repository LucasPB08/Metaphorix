package server.commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    String name;

    public ChatUser(){
        //for object mapper
    }

    public ChatUser(String name){
        this.name = name;
    }

}
