package com.example.proiectextinssac;

import com.example.proiectextinssac.domain.*;
import com.example.proiectextinssac.domain.validators.*;
import com.example.proiectextinssac.repository.Repository;
import com.example.proiectextinssac.repository.db.FriendRequestDbRepository;
import com.example.proiectextinssac.repository.db.FriendshipsDbRepository;
import com.example.proiectextinssac.repository.db.MessageDbRepository;
import com.example.proiectextinssac.repository.db.UsersDbRepository;
import com.example.proiectextinssac.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyFriendsController implements Initializable {
    Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final FriendshipsDbRepository friendshipRepository1 = new FriendshipsDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, Friendship> friendshipRepository = new FriendshipsDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, User> userRepository = new UsersDbRepository(connection, friendshipRepository1, new UserValidator());
    private final Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
    private final Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());
    private final Service service = new Service(userRepository, friendshipRepository, friendRequestRepository, messageRepository);
    User user = LoggedUser.user;
    @FXML
    private Button friendsBackButton;
    @FXML
    private TableColumn<Friendship, String> usernameTableColumn;
    @FXML
    private TableColumn<Friendship, String> friendshipDateTableColumn;
    @FXML
    private TableColumn<Friendship, String> usernameTableColumn2;
    @FXML
    private TableView<Friendship> showFriendsTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label friendsErrorLabel;
    @FXML
    private Label friendsMessageLabel;
    @FXML
    private TextField sendMessageTextField;
    private final ObservableList<Friendship> friendships = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendships.addAll(friendshipsUser());
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("usernameId1"));
        friendshipDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipDate"));
        usernameTableColumn2.setCellValueFactory(new PropertyValueFactory<>("usernameId2"));
        showFriendsTableView.setItems(friendships);
        FilteredList<Friendship> friendshipFilteredList = new FilteredList<>(friendships, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> friendshipFilteredList.setPredicate(friendship -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchKeyWord = newValue.toLowerCase();
            if (friendship.getUsernameId2().toLowerCase().contains(searchKeyWord)) {
                return true;
            } else {
                return friendship.getFriendshipDate().toString().toLowerCase().contains(searchKeyWord);
            }
        }));
        SortedList<Friendship> sortedList = new SortedList<>(friendshipFilteredList);
        sortedList.comparatorProperty().bind(showFriendsTableView.comparatorProperty());
        showFriendsTableView.setItems(sortedList);
    }

    public void deleteFriendButtonOnAction() {
        try {
            friendsErrorLabel.setText("");
            friendsMessageLabel.setText("");
            service.deleteFriendship(tableViewSelectedUser().getId());
            friendsMessageLabel.setText("You deleted a friend!");
        } catch (IllegalArgumentException | ValidationException exception) {
            friendsErrorLabel.setText(String.valueOf(exception));
        }
    }

    public List<Friendship> friendshipsUser() {
        ArrayList<Friendship> friendships = new ArrayList<>();
        for (Friendship friendship : service.printAllFriendships()) {
            if (friendship.getId1().equals(user.getId()) || friendship.getId2().equals(user.getId())) {
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    private Friendship tableViewSelectedUser() {
        Friendship selected = showFriendsTableView.getSelectionModel().getSelectedItem();
        return friendshipRepository.findOne(selected.getId());
    }

    public void sendMessageButtonOnAction() {
        try {
            friendsMessageLabel.setText("");
            friendsErrorLabel.setText("");
            service.sendMessage(user.getId(), tableViewSelectedUser().getId1(), sendMessageTextField.getText(), tableViewSelectedUser().getUsernameId2(), tableViewSelectedUser().getUsernameId1());
            friendsMessageLabel.setText("You send a message!");
        } catch (IllegalArgumentException | ValidationException exception) {
            friendsErrorLabel.setText(String.valueOf(exception));

            if (friendsErrorLabel.getText().equals(String.valueOf(exception))) {
                try {
                    friendsMessageLabel.setText("");
                    friendsErrorLabel.setText("");
                    service.sendMessage(user.getId(), tableViewSelectedUser().getId2(), sendMessageTextField.getText(), tableViewSelectedUser().getUsernameId1(), tableViewSelectedUser().getUsernameId2());
                    friendsMessageLabel.setText("You send a message!");
                } catch (IllegalArgumentException | ValidationException exception1) {
                    friendsErrorLabel.setText(String.valueOf(exception1));
                }
            }
        }
    }

    public void friendsBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendsBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
