package server;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class Clock {
    /**
     * Gets a timestamp with the current time.
     * @return Timestamp with the current time.
     */
    public Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }
}
