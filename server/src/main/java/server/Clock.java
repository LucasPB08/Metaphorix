package server;

import java.sql.Timestamp;

public class Clock {
    public Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }
}
