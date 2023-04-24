package com.example.demo;

import backend.AccountManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField newUsernameTextField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private Button updateButton;

    @FXML
    private Label warning;

    public AccountManager accountManager = new AccountManager();

    private int uid;

    public UpdateController(int uid){
        this.uid = uid;
    }

    public void navigateToLogin(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void update(ActionEvent e) throws IOException {
        System.out.println("UPDATE");

        String username = newUsernameTextField.getText();
        String password = newPasswordTextField.getText();
        if (accountManager.updateUser(uid, username, password)){
            warning.setText("Username and Password have been successfully updated!");
            navigateToLogin(e);
        } else{
            warning.setText("Error updating info!");;
        }

    }
}
