package client.utils;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import commons.Message;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimeStampHandler {
    private LocalDateTime lastDisplayedDate;

    public Label getTimeSentLabel(Message message){
        Timestamp timestamp = message.getTimestampSent();

        LocalDateTime timeSent = timestamp.toLocalDateTime();

        int hour = timeSent.getHour();
        int minute = timeSent.getMinute();

        return new Label(hour + ":" + minute);
    }

    public Label dateLabel(Message message){
        LocalDateTime messageSent = message.getTimestampSent().toLocalDateTime();

        if(this.lastDisplayedDate == null) {
            this.lastDisplayedDate = messageSent;
            return createDateLabel(messageSent);
        }

        if(this.lastDisplayedDate.getYear() != messageSent.getYear() ||
           this.lastDisplayedDate.getDayOfYear() != messageSent.getDayOfYear()) {
            this.lastDisplayedDate = messageSent;
            return createDateLabel(messageSent);
        }

        return null;
    }

    private Label createDateLabel(LocalDateTime messageDate){
        LocalDateTime today = new Timestamp(System.currentTimeMillis()).toLocalDateTime();

        if(today.getYear() == messageDate.getYear() &&
                today.getDayOfYear() == messageDate.getDayOfYear())
            return new Label("today");

        return today.getYear() > messageDate.getYear() ? dateLabelWithYear(messageDate):dateLabelWithoutYear(messageDate);
    }

    private Label dateLabelWithYear(LocalDateTime messageDate){
        int year = messageDate.getYear();
        int month = messageDate.getMonthValue();
        int dayOfMonth = messageDate.getDayOfMonth();

        String date = dayOfMonth + "/" + month + "/" + year;

        return new Label(date);
    }

    private Label dateLabelWithoutYear(LocalDateTime messageDate){
        String month = messageDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        int dayOfMonth = messageDate.getDayOfMonth();

        String date = dayOfMonth + " " + month;

        return new Label(date);
    }

    public void reset(){
        lastDisplayedDate = null;
    }
}
