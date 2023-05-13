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
    private String userName;

    private String fullName;

    private String password;

    private ChatUser(){
        //for object mapper
    }

    /**
     * Constructor for chat user
     * @param name name of user
     */
    public ChatUser(String name, String fullName){
        this.userName = name;
        this.fullName = fullName;
    }

    /**
     * Gets user's name
     * @return name
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Sets name
     * @param name new name
     */
    public void setUserName(String name){
        this.userName = name;
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
                "User Name = " + userName + "\n" +
                "Full name = " + fullName +
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
