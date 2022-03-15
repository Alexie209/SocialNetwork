package com.example.proiectextinssac;

import com.example.proiectextinssac.domain.FriendRequest;
import com.example.proiectextinssac.domain.Friendship;
import com.example.proiectextinssac.domain.Message;
import com.example.proiectextinssac.domain.User;
import com.example.proiectextinssac.domain.validators.*;
import com.example.proiectextinssac.repository.Repository;
import com.example.proiectextinssac.repository.db.FriendRequestDbRepository;
import com.example.proiectextinssac.repository.db.FriendshipsDbRepository;
import com.example.proiectextinssac.repository.db.MessageDbRepository;
import com.example.proiectextinssac.repository.db.UsersDbRepository;
import com.example.proiectextinssac.service.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
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

    @FXML
    private ImageView shieldImageView;
    @FXML
    private Button cancelButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File shieldFile = new File("images/check.png");
        Image shieldImage = new Image(shieldFile.toURI().toString());
        shieldImageView.setImage(shieldImage);
    }

    public void cancelRegistrationButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 550, 380);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerButtonOnAction(ActionEvent event) {
        try {
            validateRegistration();
            service.saveUser(firstnameTextField.getText(), lastnameTextField.getText(), usernameTextField.getText(), setPasswordField.getText());

        } catch (IllegalArgumentException | ValidationException exception) {
            registrationMessageLabel.setText(exception.getMessage());
        }
        if (registrationMessageLabel.getText().isBlank()) {
            cancelRegistrationButtonOnAction();
        }

    }

    public void validateRegistration() {
        String password = setPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!Objects.equals(password, confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
        if (usernameTextField.getText().isBlank() || firstnameTextField.getText().isBlank() || setPasswordField.getText().isBlank() || confirmPasswordField.getText().isBlank()) {
            throw new IllegalArgumentException("You must enter all fields!");
        }
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(usernameTextField.getText())) {
                throw new IllegalArgumentException("This username already exists!");
            }
        }
    }
}
