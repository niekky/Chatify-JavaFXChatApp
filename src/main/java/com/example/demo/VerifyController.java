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


public class VerifyController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField verifyUsernameTextField;

    @FXML
    private TextField verifyPasswordTextField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label warning;

    public AccountManager accountManager = new AccountManager();



    public void navigateToUpdate(ActionEvent e, int uid) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("update.fxml"));
        loader.setControllerFactory(c -> new UpdateController(uid));

        Parent root = loader.load();


        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void verify(ActionEvent e) throws IOException {
        System.out.println("VERIFY");

        String username = verifyUsernameTextField.getText();
        String password = verifyPasswordTextField.getText();
        int uid = accountManager.verifyUser(username, password);
        if (uid!=-1){
            warning.setText("Verfication Success!");
            navigateToUpdate(e, uid);
        } else warning.setText("Verfication Failed!");

    }


}
