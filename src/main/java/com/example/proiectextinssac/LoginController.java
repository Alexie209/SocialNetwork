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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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
    private final Service service=new Service(userRepository,friendshipRepository,friendRequestRepository,messageRepository);

    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/socialize-icon-1153.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("images/lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction(ActionEvent event) throws SQLException {

        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter username or password");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() throws SQLException {
        boolean ok=false;
        for (User user:service.printAll()){
            if (user.getUsername().equals(usernameTextField.getText()) && user.getPassword().equals(passwordTextField.getText())){
                loginMessageLabel.setText("Congratulations!");
                ok=true;

                LoggedUser.setUser(user);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("accountController.fxml"));
                Scene scene;
                try {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                    scene = new Scene(fxmlLoader.load(), 650, 489);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!ok){
            loginMessageLabel.setText("Invalid login!");
        }
    }

//    public void createAccountForm() {
//        try {
//            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
//            Stage registerStage = new Stage();
//            registerStage.initStyle(StageStyle.UNDECORATED);
//            registerStage.setScene(new Scene(root, 520, 552));
//            registerStage.show();
////            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("register.fxml"));
////            Scene scene = new Scene(fxmlLoader.load(), 520, 552);
////            Stage registerStage = new Stage();
////            registerStage.initStyle(StageStyle.UNDECORATED);
////            registerStage.setScene(scene);
////            registerStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getCause();
//        }
//    }

    public void registerButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 552);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}