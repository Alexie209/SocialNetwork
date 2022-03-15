package com.example.proiectextinssac;

import com.example.proiectextinssac.domain.*;
import com.example.proiectextinssac.domain.validators.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateAccountController {
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
    private Button finishUpdateAccountButton;
    @FXML
    private Button backUserUpdateButton;
    @FXML
    private Label errorMessageUserUpdateLabel;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private TextField newUsernameTextField;
    @FXML
    private TextField newFirstNameTextField;
    @FXML
    private TextField newLastNameTextField;

    public void finishUpdateAccountButtonOnAction() {
        try {
            service.updateUser(user.getId(), newFirstNameTextField.getText(), newLastNameTextField.getText(), newUsernameTextField.getText(), newPasswordTextField.getText());
        } catch (IllegalArgumentException | ValidationException exception) {
            errorMessageUserUpdateLabel.setText(exception.getMessage());
        }
        if (errorMessageUserUpdateLabel.getText().isBlank()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
            Scene scene;
            try {
                Stage stage = (Stage) finishUpdateAccountButton.getScene().getWindow();
                stage.close();
                scene = new Scene(fxmlLoader.load(), 650, 489);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void updateAccountBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) backUserUpdateButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
