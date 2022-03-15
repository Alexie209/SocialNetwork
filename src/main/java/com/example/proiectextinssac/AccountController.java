package com.example.proiectextinssac;

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
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView iconImageView;
    @FXML
    private ImageView settingsImageView;
    @FXML
    private Button settingsButton;
    @FXML
    private Button accountControllerLogoutButton;
    @FXML
    private Button accountControllerExitButton;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button friendRequestsButton;
    @FXML
    private Button myFriendsButton;
    @FXML
    private Button messagesButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("images/socialize-icon-1153.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File iconFile = new File("images/account.png");
        Image iconImage = new Image(iconFile.toURI().toString());
        iconImageView.setImage(iconImage);

        File settingsFile = new File("images/settings.png");
        Image settingsImage = new Image(settingsFile.toURI().toString());
        settingsImageView.setImage(settingsImage);
    }

    public void settingsButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) settingsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountControllerLogoutButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) accountControllerLogoutButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 550, 380);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountControllerExitButtonOnAction() {
        Stage stage = (Stage) accountControllerExitButton.getScene().getWindow();
        stage.close();
    }

    public void addFriendButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addFriend.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) addFriendButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void friendRequestsButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friendRequests.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendRequestsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myFriendsButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("myFriends.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) myFriendsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messagesButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("messages.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) messagesButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 650, 489);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
