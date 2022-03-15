package com.example.proiectextinssac.domain;

import java.time.LocalDate;

public class FriendRequest extends Entity<Long> {

    private Long id1;
    private Long id2;
    private String status;
    private String usernameId1;
    private String usernameId2;
    private LocalDate friendRequestDate;

    public FriendRequest(Long id1, Long id2,String status, LocalDate friendRequestDate, String usernameId1, String usernameId2) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = status;
        this.friendRequestDate = friendRequestDate;
        this.usernameId1 = usernameId1;
        this.usernameId2 = usernameId2;
    }

    public Long getId1() {
        return this.id1;
    }

    public Long getId2() {
        return this.id2;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getStatus(){return this.status;}

    public void setStatus(String status){this.status = status;}

    public LocalDate getFriendRequestDate() {return this.friendRequestDate; }

    public void setFriendRequestDate(LocalDate friendRequestDate) {this.friendRequestDate = friendRequestDate;}

    public String getUsernameId1() {return this.usernameId1;}

    public void setUsernameId1(String usernameId1) {this.usernameId1 = usernameId1;}

    public String getUsernameId2() {return this.usernameId2;}

    public void setUsernameId2(String usernameId2) {this.usernameId2 = usernameId2;}

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id = '" + id + '\'' +
                ", id1='" + id1 + '\'' +
                ", id2='" + id2 + '\'' +
                ", status='" + status + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendRequest that)) return false;
        return getId1().equals(that.getId1())
                && getId2().equals(that.getId2())
                && getStatus().equals(that.getStatus())
                && getFriendRequestDate().equals(that.getFriendRequestDate())
                && getUsernameId1().equals(that.getUsernameId1())
                && getUsernameId2().equals(that.getUsernameId2());
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
