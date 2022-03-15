package com.example.proiectextinssac.repository.db;

import com.example.proiectextinssac.domain.FriendRequest;
import com.example.proiectextinssac.domain.validators.Validator;
import com.example.proiectextinssac.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository implements Repository<Long, FriendRequest> {

    private final Connection connection;
    private final Validator<FriendRequest> validator;
    private final static String ID1 = "friend_one_id";
    private final static String ID2 = "friend_two_id";
    private final static String STATUS = "status";
    private final static String USERNAMEID1="usernameid1";
    private final static String USERNAMEID2="usernameid2";
    private final static String FRIENDREQUESTDATE = "friendrequestdate";


    public FriendRequestDbRepository(Connection connection, Validator<FriendRequest> validator) {
        this.connection = connection;
        this.validator = validator;

    }

    @Override
    public FriendRequest save(FriendRequest entity) {
        boolean exists = false;
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");

        for (FriendRequest friendRequest : findAll()) {
            if (entity.equals(friendRequest) || (entity.getId1().equals(friendRequest.getId2()) && entity.getId2().equals(friendRequest.getId1()))) {
                exists = true;
                break;
            }
        }
        if (exists) {
            throw new IllegalArgumentException("friendship already exists!");
        }

        validator.validate(entity);
        String sql = "insert into friend_requests ( friend_one_id, friend_two_id, status, friendRequestDate, usernameId1, usernameId2) values (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, entity.getId1());
            preparedStatement.setLong(2, entity.getId2());
            preparedStatement.setString(3, String.valueOf(entity.getStatus()));
            preparedStatement.setDate(4, Date.valueOf(entity.getFriendRequestDate()));
            preparedStatement.setString(5, entity.getUsernameId1());
            preparedStatement.setString(6, entity.getUsernameId2());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public FriendRequest delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from friend_requests where id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        String sql = "select * from friend_requests where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long friendOneId = resultSet.getLong(ID1);
            Long friendTwoId = resultSet.getLong(ID2);
            String status = resultSet.getString(STATUS);
            LocalDate friendRequestDate = LocalDate.parse(String.valueOf(resultSet.getDate(FRIENDREQUESTDATE)));
            String usernameId1 = resultSet.getString(USERNAMEID1);
            String usernameId2 = resultSet.getString(USERNAMEID2);
            FriendRequest friendRequest = new FriendRequest(friendOneId, friendTwoId, status, friendRequestDate, usernameId1, usernameId2);
            friendRequest.setId(id);
            return friendRequest;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(FriendRequest entity) {

    }

    @Override
    public List<FriendRequest> findAll() {
        List<FriendRequest> friendships = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from friend_requests");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id =resultSet.getLong("id");
                Long friendOneId = resultSet.getLong(ID1);
                Long friendTwoId = resultSet.getLong(ID2);
                String status = resultSet.getString(STATUS);
                LocalDate friendRequestDate = LocalDate.parse(String.valueOf(resultSet.getDate(FRIENDREQUESTDATE)));
                String usernameId1 = resultSet.getString(USERNAMEID1);
                String usernameId2 = resultSet.getString(USERNAMEID2);
                FriendRequest friendship = new FriendRequest(friendOneId, friendTwoId, status, friendRequestDate, usernameId1, usernameId2);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public List<FriendRequest> getFriends(FriendRequest friendRequest) {
        return null;
    }

    @Override
    public FriendRequest getByUsername(String s) {
        return null;
    }

}
