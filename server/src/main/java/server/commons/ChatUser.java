package server.commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class ChatUser {

    @Id
    private String name;

    private String password;

    private ChatUser(){
        //for object mapper
    }

    /**
     * Constructor for chat user
     * @param name name of user
     */
    public ChatUser(String name){
        this.name = name;
    }

    /**
     * Gets user's name
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets name
     * @param name new name
     */
    public void setName(String name){
        this.name = name;
    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }

    /**
     * Creates an int from this object
     * @return int
     */
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Makes a string representation from this user
     * @return a string representation
     */
    @Override
    public String toString(){
        return "ChatUser{" +
                "Name = " + name +
                "}";
    }

    /**
     * Checks for equality between this and other
     * @param other other object to check equality with
     * @return true if this and other are equals, false otherwise.
     */
    @Override
    public boolean equals(Object other){
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
