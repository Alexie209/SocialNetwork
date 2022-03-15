package com.example.proiectextinssac.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private LocalDate date;
    public Long reply;
    private String to_username;
    private String from_username;

    public Message(Long from, Long to, String message, LocalDate date, Long reply, String to_username, String from_username) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
        this.to_username = to_username;
        this.from_username = from_username;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long aLong) {
        this.from = aLong;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long aLong1) {
        this.to = aLong1;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public String getTo_username() {return this.to_username;}

    public String getFrom_username() {return this.from_username;}

    @Override
    public String toString() {
        return "Message{" +
                "id = '" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", reply='" + reply + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message that)) return false;
        return getFrom().equals(that.getFrom())
                && getTo().equals(that.getTo())
                && getMessage().equals(that.getMessage())
                && getDate().equals(that.getDate())
                && getReply().equals(that.getReply())
                && getTo_username().equals(that.getTo_username())
                && getFrom_username().equals(that.getFrom_username());
    }


    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date, reply, to_username, from_username);
    }

    public int compare(Message message) {
        if (this.getDate() == null || message.getDate() == null) {
            return 0;
        }
        return this.getDate().compareTo(message.getDate());
    }

}



