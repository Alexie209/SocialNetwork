package com.example.proiectextinssac;

import com.example.proiectextinssac.domain.*;
import com.example.proiectextinssac.domain.validators.FriendRequestValidator;
import com.example.proiectextinssac.domain.validators.FriendshipValidator;
import com.example.proiectextinssac.domain.validators.MessageValidator;
import com.example.proiectextinssac.domain.validators.UserValidator;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;

public class MessagesController implements Initializable {
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
    private TextField showMessagesSearchTextField;
    @FXML
    private TableView<Message> showMessagesTableView;
    @FXML
    private Button showMessagesBackButton;
    @FXML
    private TableColumn<Message, String> showMessageUsernameFrom;
    @FXML
    private TableColumn<Message, String> showMessagesMessage;
    @FXML
    private ImageView brandingMessagesImageView;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/messages.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingMessagesImageView.setImage(brandingImage);
        messages.addAll(messagesUser());
        showMessageUsernameFrom.setCellValueFactory(new PropertyValueFactory<>("from_username"));
        showMessagesMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        showMessagesTableView.setItems(messages);
        FilteredList<Message> messageFilteredList = new FilteredList<>(messages, b -> true);
        showMessagesSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> messageFilteredList.setPredicate(message -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            if (message.getFrom_username().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return message.getMessage().toString().toLowerCase().contains(searchkeyword);
        }));
        SortedList<Message> sortedList = new SortedList<>(messageFilteredList);
        sortedList.comparatorProperty().bind(showMessagesTableView.comparatorProperty());
        showMessagesTableView.setItems(sortedList);

    }

    public List<Message> messagesUser() {
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message : service.printAllMessages()) {
            if (message.getTo().equals(user.getId())) {
                messages.add(message);
            }
        }
        return messages;
    }

    public void showMessagesBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) showMessagesBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
