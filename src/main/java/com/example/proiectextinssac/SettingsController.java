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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
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
    private Button updateAccountButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button userSettingsBackButton;

    @FXML
    private ImageView settingsImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/settings-logo.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        settingsImageView.setImage(brandingImage);
    }

    public void deleteAccountOnAction() {
        service.deleteUser(user.getId());
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) deleteAccountButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 550, 380);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAccountOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("update_account.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) updateAccountButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 552);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backUserSettingsButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) userSettingsBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

