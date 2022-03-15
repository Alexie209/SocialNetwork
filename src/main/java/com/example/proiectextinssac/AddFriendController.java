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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddFriendController implements Initializable {
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
    private TextField searchFriendTextField;
    @FXML
    private TableView<User> addFriendTableView;
    @FXML
    private TableColumn<User, String> firstNameTableColumn;
    @FXML
    private TableColumn<User, String> lastNameTableColumn;
    @FXML
    private TableColumn<User, String> usernameTableColumn;
    @FXML
    private Label messageLabel;
    @FXML
    private Button addFriendBackButton;
    @FXML
    private ImageView addFriendImageView;
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/add-friends.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        addFriendImageView.setImage(brandingImage);
        users.setAll(appUsers());
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        addFriendTableView.setItems(users);
        FilteredList<User> userFilteredList = new FilteredList<>(users, b -> true);
        searchFriendTextField.textProperty().addListener((observable, oldValue, newValue) -> userFilteredList.setPredicate(user -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            if (user.getFirstName().toLowerCase().contains(searchkeyword)) {
                return true;

            } else if (user.getLastName().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return user.getUsername().toLowerCase().contains(searchkeyword);
        }));
        SortedList<User> sortedList = new SortedList<>(userFilteredList);
        sortedList.comparatorProperty().bind(addFriendTableView.comparatorProperty());
        addFriendTableView.setItems(sortedList);
    }

    public List<User> appUsers() {
        ArrayList<User> appUsers = new ArrayList<>();
        for (User user1 : service.printAll()) {
            if (!Objects.equals(user1.getId(), user.getId())) {
                appUsers.add(user1);
            }
        }
        return appUsers;
    }

    private User tableViewSelectedUser() {
        User selected = addFriendTableView.getSelectionModel().getSelectedItem();
        return userRepository.getByUsername(selected.getUsername());
    }

    public void sendFriendRequestButtonOnAction() {
        try {
            messageLabel.setText("");
            sendFriendRequestValidator();
            User user1 = tableViewSelectedUser();
            Long id1 = user1.getId();
            //System.out.println(tableViewSelectedUser().getId());
            service.sendFriendRequest(user.getId(), user1.getId(), user1.getUsername(), user.getUsername());
            messageLabel.setText("The request was succesfully send!");
        } catch (IllegalArgumentException | ValidationException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    public void sendFriendRequestValidator() {
        for (FriendRequest friendRequest : service.printAllFriendRequests()) {
            if (friendRequest.getId2().equals(tableViewSelectedUser().getId()) && friendRequest.getId().equals(user.getId())) {
                throw new IllegalArgumentException("The request was already send!");
            }
        }
    }

    public void addFriendBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) addFriendBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

