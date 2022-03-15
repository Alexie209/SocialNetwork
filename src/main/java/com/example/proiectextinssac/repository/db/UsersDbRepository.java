package com.example.proiectextinssac.repository.db;

import com.example.proiectextinssac.domain.Friendship;
import com.example.proiectextinssac.domain.User;
import com.example.proiectextinssac.domain.validators.Validator;
import com.example.proiectextinssac.repository.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersDbRepository implements Repository<Long, User> {
    protected Map<Long, User> entities;

    private final Validator<User> validator;

    private final FriendshipsDbRepository friendshipsDbRepository;

    private final Connection connection;

    public UsersDbRepository(Connection connection, FriendshipsDbRepository friendshipsDbRepository, Validator<User> validator) {
        this.connection = connection;
        this.friendshipsDbRepository = friendshipsDbRepository;
        this.validator = validator;

        entities = new HashMap<>();
    }


    @Override
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into users (first_name, last_name, username, password ) values (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUsername());
            ps.setString(4, entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User user = new User(entity.getFirstName(), entity.getLastName(), entity.getUsername(), entity.getPassword());
        entities.put(entity.getId(), user);

        return null;
    }

    @Override
    public User delete(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("deleted entity doesn't exist");
        }

        String sql = "delete from users where id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Friendship> friendships = friendshipsDbRepository.findAll();
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(aLong) || friendship.getId2().equals(aLong)) {
                friendshipsDbRepository.delete(friendship.getId());
            }
        }
        entities.remove(aLong);

        return null;
    }

    @Override
    public void update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "update users set first_name = ?, last_name = ?, username = ?, password = ? where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setLong(5, entity.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        entities.put(entity.getId(), entity);
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
        }
    }

    @Override
    public User findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select * from users where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            return new User(firstName, lastName, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(firstName, lastName, username, password);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getFriends(User user) {
        List<Friendship> friendships = friendshipsDbRepository.findAll();
        List<User> users = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(user.getId())) {
                User user1=findOne(friendship.getId2());
                user1.setId(friendship.getId2());
                users.add(user1);
            }
            if (friendship.getId2().equals(user.getId())){
                User user1=findOne(friendship.getId1());
                user1.setId(friendship.getId2());
                users.add(user1);
            }
        }
        return users;
    }
    @Override
    public User getByUsername(String username) {
        String sql = "select * from users where username = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String passwordHash = resultSet.getString("password");
            User user = new User(firstName, lastName, username, passwordHash);
            user.setId(id);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}