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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FriendRequestsController implements Initializable {
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
    private TableView<FriendRequest> friendRequestTableView;
    @FXML
    private TableColumn<FriendRequest, String> friendRequestUsernameTableColumn;
    @FXML
    private TableColumn<FriendRequest, String> friendRequestStatusTableColumn;
    @FXML
    private TableColumn<FriendRequest, String> friendrequestDateTableColumn;
    @FXML
    private Button friendRequestBackButton;
    @FXML
    private Label friendRequestMessageLabel;
    @FXML
    private Label FriendRequestAcceptLabel;
    @FXML
    private TextField friendRequestsSearchTextField;
    @FXML
    private ImageView friendRequestBrandingImage;
    private final ObservableList<FriendRequest> friendRequests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/friend-request.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        friendRequestBrandingImage.setImage(brandingImage);

        friendRequests.setAll(friendRequestsUser());
        friendRequestUsernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("usernameId2"));
        friendRequestStatusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        friendrequestDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("friendRequestDate"));
        friendRequestTableView.setItems(friendRequests);
        FilteredList<FriendRequest> friendRequestFilteredList = new FilteredList<>(friendRequests, b -> true);
        friendRequestsSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> friendRequestFilteredList.setPredicate(friendRequest -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            User userFriendRequest = service.findOne(friendRequest.getId2());
            if (userFriendRequest.getUsername().toLowerCase().contains(searchkeyword)) {
                return true;

            } else if (friendRequest.getStatus().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return friendRequest.getFriendRequestDate().toString().toLowerCase().contains(searchkeyword);
        }));
        SortedList<FriendRequest> sortedList = new SortedList<>(friendRequestFilteredList);
        sortedList.comparatorProperty().bind(friendRequestTableView.comparatorProperty());
        friendRequestTableView.setItems(sortedList);
    }

    public List<FriendRequest> friendRequestsUser() {
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
        for (FriendRequest friendRequest : service.printAllFriendRequests()) {
            if (friendRequest.getId2().equals(user.getId())) {
                friendRequests.add(friendRequest);
            }
        }
        return friendRequests;
    }

    public void friendRequestAcceptButtonOnAction() {
        try {
            friendRequestMessageLabel.setText("");
            FriendRequest friendRequest = tableViewSelectedUser();
            service.updateFriendRequest(tableViewSelectedUser().getId(), Status.APPROVED);
            FriendRequestAcceptLabel.setText("You Accepted the friend request");
        } catch (IllegalArgumentException | ValidationException exception) {
            friendRequestMessageLabel.setText(String.valueOf(exception));
        }
    }

    private FriendRequest tableViewSelectedUser() {
        FriendRequest selected = friendRequestTableView.getSelectionModel().getSelectedItem();
        return friendRequestRepository.findOne(selected.getId());
    }

    public void friendRequestDeclineButtonOnAction() {
        try {
            friendRequestMessageLabel.setText("");
            FriendRequestAcceptLabel.setText("");
            service.updateFriendRequest(tableViewSelectedUser().getId(), Status.REJECTED);
            FriendRequestAcceptLabel.setText("You rejected the friend request");
        } catch (IllegalArgumentException | ValidationException exception) {
            friendRequestMessageLabel.setText(String.valueOf(exception));
        }
    }

    public void friendRequestBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendRequestBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
