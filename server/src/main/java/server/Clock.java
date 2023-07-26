package server;

import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

@Controller
public class Clock {
    public Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }
}
