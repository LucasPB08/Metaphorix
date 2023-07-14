package client.utils;

import commons.Message;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeStampHandler {

    public Label getTimeSentLabel(Message message){
        Timestamp timestamp = message.getTimestampSent();

        LocalDateTime timeSent = timestamp.toLocalDateTime();

        int hour = timeSent.getHour();
        int minute = timeSent.getMinute();

        return new Label(hour + ":" + minute);
    }
}
