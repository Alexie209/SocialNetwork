package com.example.proiectextinssac.domain;


import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private final Long id1;
    private final Long id2;
    private final String usernameId1;
    private final String usernameId2;
    private LocalDate friendshipDate;

    public Friendship(Long id1, Long id2,LocalDate friendshipDate, String usernameId1, String usernameId2) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendshipDate = friendshipDate;
        this.usernameId1 = usernameId1;
        this.usernameId2 = usernameId2;
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public LocalDate getFriendshipDate(){return this.friendshipDate;}

    public String getUsernameId1() {return this.usernameId1;}

    public String getUsernameId2() {return this.usernameId2;}

    public void setFriendshipDate(LocalDate friendshipDate1){this.friendshipDate = friendshipDate1;}
    @Override
    public String toString() {
        return "Friendship{" +
                "id friendship=" + id + '\'' +
                ", id1=" + id1 + '\'' +
                ", id2=" + id2 + '\'' +
                ", friendshipDate=" + friendshipDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship friendship = (Friendship) o;
        return Objects.equals(id1, friendship.id1)
                && Objects.equals(id2, friendship.id2)
                && Objects.equals(friendshipDate, friendship.friendshipDate)
                && Objects.equals(usernameId1, friendship.usernameId1)
                && Objects.equals(usernameId2, friendship.usernameId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, usernameId2);
    }
}
