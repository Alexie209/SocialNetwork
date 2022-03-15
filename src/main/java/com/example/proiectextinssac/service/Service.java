package com.example.proiectextinssac.service;


import com.example.proiectextinssac.domain.*;
import com.example.proiectextinssac.repository.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {
    private final Repository<Long, User> userRepository;
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, FriendRequest> friendRequestRepository;
    private final Repository<Long, Message> messageRepository;


    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, FriendRequest> friendRequestRepository, Repository<Long, Message> messageRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.messageRepository = messageRepository;
    }

    public void saveUser(String firstName, String lastName, String username, String password) {
        User user = new User(firstName, lastName, username, password);
        this.userRepository.save(user);
    }

    public void updateUser(Long id, String firstName, String lastName, String username, String password) {
        for (User user : this.userRepository.findAll()) {
            if (user.getId().equals(id)) {
                User user1 = new User(firstName, lastName, username, password);
                user1.setId(user.getId());
                this.userRepository.update(user1);

            }
        }
    }

    public void saveFriendship(Long id1, Long id2, LocalDate friendRequestDate, String usernameId1, String usernameId2) {
        boolean ok = true;
        if (id1 == null || id2 == null) {
            throw new IllegalArgumentException("Id's must be not null");
        }
        User user1 = userRepository.findOne(id1);
        User user2 = userRepository.findOne(id2);
        user1.setId(id1);
        user2.setId(id2);
        for (User user : user1.getFriends()) {
            if (Objects.equals(user.getId(), id2)) {
                ok = false;
                break;
            }
        }
        for (User user : user2.getFriends()) {
            if (Objects.equals(user.getId(), id1)) {
                ok = false;
                break;
            }
        }
        if (ok) {
            user1.makeFriend(user2);
            user2.makeFriend(user1);
            this.friendshipRepository.save(new Friendship(id1, id2, LocalDate.now(),usernameId1, usernameId2));
        }
    }

    public void deleteUser(Long id) {
        this.userRepository.delete(id);
    }

    public void deleteFriendship(Long id) {
        this.friendshipRepository.delete(id);
    }

    public User findOne(Long x) {
        return this.userRepository.findOne(x);
    }

    public List<User> printAll() {
        return this.userRepository.findAll();
    }

    public List<User> getFriends(User user) {
        return this.userRepository.getFriends(user);
    }

    public List<Friendship> printAllFriendships() {
        return this.friendshipRepository.findAll();
    }

    public List<Friendship> getFriendshipRelations(Long aLong) {
        List<Friendship> getAll = friendshipRepository.findAll();
        Predicate<Friendship> filterCriteria = x -> Objects.equals(x.getId1(), aLong) || Objects.equals(x.getId2(), aLong);
        return getAll.stream()
                .filter(filterCriteria)
                .toList();
    }

    public List<Friendship> getFriendshipRelationsByMonth(Long aLong, int month) {
        List<Friendship> getAll = friendshipRepository.findAll();
        Predicate<Friendship> filterById = x -> Objects.equals(x.getId1(), aLong) || Objects.equals(x.getId2(), aLong);
        Predicate<Friendship> filterByMonth = x -> x.getFriendshipDate().getMonthValue() == month;
        Predicate<Friendship> filterCriteria = filterById.and(filterByMonth);
        return getAll.stream()
                .filter(filterCriteria)
                .toList();
    }

    public void sendMessage(Long from, Long to, String text, String toUsername, String fromUsername) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Id's must be not null");
        }
        List<Message> conversation = showConversation(from, to);
        Message newMessage = new Message(from, to, text, LocalDate.now(), 0L, toUsername, fromUsername);
        if (conversation.size() == 0 || conversation.get(conversation.size() - 1).getFrom().equals(from)) {
            newMessage.setReply(0L);
        } else {
            newMessage.setReply(to);
        }
        this.messageRepository.save(newMessage);
    }

    public void deleteMessage(Long id) {
        this.messageRepository.delete(id);
    }

    public List<Message> printAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> showConversation(Long id1, Long id2) {
        List<Message> conversation = new ArrayList<>();
        for (Message message : printAllMessages()) {
            if ((message.getFrom().equals(id1) && message.getTo().equals(id2)) || (message.getFrom().equals(id2) && message.getTo().equals(id1))) {
                conversation.add(message);
            }
        }
        List<Message> sortedMessages = conversation.stream()
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

        return sortedMessages;
    }


    public void sendFriendRequest(Long id1, Long id2, String usernameId1, String usernameId2) {
        boolean ok = true;
        if (id1 == null || id2 == null) {
            throw new IllegalArgumentException("Id's must be not null");
        }
        List<Friendship> getAll = printAllFriendships();
        for (Friendship friendship : getAll) {
            if ((friendship.getId1().equals(id1) && friendship.getId2().equals(id2)) || friendship.getId1().equals(id2) && friendship.getId2().equals(id1)) {
                ok = false;
                break;
            }
        }
        if (ok) {
            String status = String.valueOf(Status.PENDING);
            LocalDate friendRequestDate =LocalDate.now();
            this.friendRequestRepository.save(new FriendRequest(id1, id2, status, friendRequestDate, usernameId1, usernameId2));
        } else {
            throw new IllegalArgumentException("The friend request can't be sent!Friendship already exists");
        }
    }

    public List<FriendRequest> printAllFriendRequests() {
        return this.friendRequestRepository.findAll();
    }

    public void deleteFriendRequests(Long id) {
        this.friendRequestRepository.delete(id);
    }

    public void updateFriendRequest(Long id, Status status) {
        FriendRequest friendRequest = this.friendRequestRepository.findOne(id);
        if (status == Status.REJECTED) {
            this.friendRequestRepository.delete(id);
        } else if (status == Status.APPROVED) {
            deleteFriendRequests(id);
            this.saveFriendship(friendRequest.getId1(), friendRequest.getId2(), friendRequest.getFriendRequestDate(), friendRequest.getUsernameId1(), friendRequest.getUsernameId2());
        }
    }

}
