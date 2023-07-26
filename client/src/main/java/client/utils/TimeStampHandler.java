package client.utils;

import commons.GroupMessage;
import commons.Message;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class TimeStampHandler {
    private LocalDateTime lastDisplayedDate;

    /**
     * Gets the label that is displayed together with a message, indicating
     * the time it was sent at.
     * @param message The message that was sent.
     * @return Label holding the time stamp.
     */
    public Label getTimeSentLabel(Message message){
        Timestamp timestamp = message.getTimestampSent();

        LocalDateTime timeSent = timestamp.toLocalDateTime();

        int hour = timeSent.getHour();
        int minute = timeSent.getMinute();

        return minute <= 9 ? new Label(hour + ":0" + minute) :
                new Label(hour + ":" + minute);
    }

    /**
     * Gets the label that is displayed together with a message, indicating
     * the time it was sent at.
     * @param message The message that was sent.
     * @return Label holding the time stamp.
     */
    public Label getTimeSentLabel(GroupMessage message){
        Timestamp timestamp = message.getTimestampSent();

        LocalDateTime timeSent = timestamp.toLocalDateTime();

        int hour = timeSent.getHour();
        int minute = timeSent.getMinute();

        return minute <= 9 ? new Label(hour + ":0" + minute) :
                new Label(hour + ":" + minute);
    }

    /**
     *  Creates the label that is shown to separate messages sent on different days.
     * @param message A message from the chat.
     * @return Null if the previous loaded message was on the same day, otherwise
     * a label with the date of the message.
     */
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

    /**
     *  Creates the label that is shown to separate messages sent on different days.
     * @param message A message from the chat.
     * @return Null if the previous loaded message was on the same day, otherwise
     * a label with the date of the message.
     */
    public Label dateLabel(GroupMessage message){
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

        return today.getYear() > messageDate.getYear() ?
                dateLabelWithYear(messageDate):dateLabelWithoutYear(messageDate);
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

    /**
     * This is called when a new chat is loaded,
     * as there hasn't been a last displayed date.
     */
    public void reset(){
        lastDisplayedDate = null;
    }
}
