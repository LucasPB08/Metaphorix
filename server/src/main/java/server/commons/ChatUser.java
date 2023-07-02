package server.commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ChatUser {

    @Id
    private String userName;

    private String fullName;

    private String password;

    @OneToMany(mappedBy = "initiator")
    private List<Chat> initiatedChats;

    @OneToMany(mappedBy = "receiver")
    private List<Chat> receivedChats;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    private ChatUser(){
        //for object mapper
    }

    /**
     * Constructor for ChatUser without password
     * @param userName username
     * @param fullName full name
     */
    public ChatUser(String userName, String fullName){
        this.userName = userName;
        this.fullName = fullName;
    }

    /**
     * Constructor for a new user
     * @param name username of user
     * @param fullName full name of user
     * @param password password of user
     */
    public ChatUser(String name, String fullName, String password){
        this.userName = name;
        this.fullName = fullName;
        this.password = password;
        this.receivedChats = new ArrayList<>();
        this.initiatedChats = new ArrayList<>();
    }

    /**
     * Gets user's name
     * @return name
     */
    public String getUserName(){
        return userName;
    }

    /**
     * Gets full name of user
     * @return full name
     */
    public String getFullName(){
        return fullName;
    }

    /**
     * Sets name
     * @param name new name
     */
    public void setUserName(String name){
        this.userName = name;
    }

    public List<Message> getMessages(){
        return this.messages;
    }

    /**
     * Checks whether the password used is this user's password
     * @param password password input
     * @return true if the password corresponds to the user's password, false otherwise.
     */
    public boolean validatePassword(String password){
        return this.password.equals(password);
    }

    public List<Chat> allChats(){
        List<Chat> chats = new ArrayList<>();
        if(this.initiatedChats != null) chats.addAll(this.initiatedChats);
        if(this.receivedChats != null) chats.addAll(this.receivedChats);

        return chats;
    }

    public void addInitiatedChat(Chat chat){
        this.initiatedChats.add(chat);
    }

    public void addReceivedChat(Chat chat){
        this.receivedChats.add(chat);
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
