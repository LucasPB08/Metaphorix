package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    String name;

    public ChatUser(){
    }

    public ChatUser(String name){
        this.name = name;
    }

}
