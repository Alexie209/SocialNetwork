package com.example.proiectextinssac.repository.db;

import com.example.proiectextinssac.domain.Message;
import com.example.proiectextinssac.domain.validators.Validator;
import com.example.proiectextinssac.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDbRepository implements Repository<Long, Message> {

    protected Map<Long, Message> entities;

    private final Connection connection;

    private final Validator<Message> validator;

    private final static String TO_USERNAME="to_username";
    private final static String FROM_USERNSME="from_username";


    public MessageDbRepository(Connection connection, Validator<Message> validator) {
        this.connection = connection;
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Message save(Message entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into messages ( from_user,to_user, message,message_date, reply, to_username, from_username ) values (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3,entity.getMessage());
            ps.setDate(4,Date.valueOf(String.valueOf(entity.getDate())));
            ps.setLong(5, entity.getReply());
            ps.setString(6, entity.getTo_username());
            ps.setString(7, entity.getFrom_username());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from messages where id = ?";
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
    public void update(Message entity) {
    }

    @Override
    public Message findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select * from messages where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, aLong);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long from = resultSet.getLong("from_user");
            Long to = resultSet.getLong("to_user");
            String message = resultSet.getString("message");
            String date1 = String.valueOf(resultSet.getDate("message_date"));
            LocalDate date = LocalDate.parse(date1);
            Long reply = resultSet.getLong("reply");
            String toUsername = resultSet.getString(TO_USERNAME);
            String fromUsername = resultSet.getString(FROM_USERNSME);

            return new Message(from, to, message,date,reply, toUsername, fromUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");
                String message = resultSet.getString("message");
                String date1 = String.valueOf(resultSet.getDate("message_date"));
                LocalDate date = LocalDate.parse(date1);
                Long reply = resultSet.getLong("reply");
                String toUsername = resultSet.getString(TO_USERNAME);
                String fromUsername = resultSet.getString(FROM_USERNSME);
                Message message1 = new Message(from, to,message, date, reply, toUsername, fromUsername);
                message1.setId(id);
                messages.add(message1);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public List<Message> getFriends(Message user) {
        return null;
    }

    @Override
    public Message getByUsername(String s) {
        return null;
    }
}
