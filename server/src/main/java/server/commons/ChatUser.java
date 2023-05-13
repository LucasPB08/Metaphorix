package server.commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private ChatUser(){
        //for object mapper
    }

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
    /**
     * Gets user's id.
     */
    public long getId(){
        return id;
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
                ", ID = " + id +
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
