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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AccController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private TextField loginPasswordTextField;

    @FXML
    private TextField signupUsernameTextField;

    @FXML
    private TextField signupPasswordTextField;

    @FXML
    private TextField signupConPasswordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private Label warning;

    @FXML
    private Label warningSignup;

    public AccountManager accountManager = new AccountManager();

    public void navigateToLogin(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void navigateToSignup(ActionEvent e) throws  IOException{
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void navigateToRooms(ActionEvent e, String username) throws IOException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("rooms.fxml"));
//        Parent root = loader.load();
//
//        RoomController roomController = loader.getController();
//        roomController.passUID(accountManager.getCurrentUserID());

//        RoomController roomController = new RoomController(accountManager.getCurrentUserID());
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rooms.fxml"));
//        fxmlLoader.setController(roomController);
//        Parent root = fxmlLoader.load();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("rooms.fxml"));
        loader.setControllerFactory(c -> new RoomController(accountManager.getCurrentUserID(), username));

        Parent root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent e) throws IOException {
        System.out.println("LOGIN");

        String username = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();

        boolean loginSuccess = accountManager.login(username, password);

        if (loginSuccess){
            warning.setText("Login Successfully!");
            navigateToRooms(e, username);
        }else if(Objects.equals(username, "") && Objects.equals(password, "")) {
            warning.setText("No username or password!");
            System.out.println("No username or password!");
        }else if(Objects.equals(username, "")){
            warning.setText("You forgot your username!");
            System.out.println("You forgot your username!");
        }else if(Objects.equals(password, "")){
            warning.setText("You forgot your password!");
            System.out.println("You forgot your password!");
        }else{
            warning.setText("Incorrect username or password!");
            System.out.println("Incorrect username or password!");
        }


    }

    public void signup(ActionEvent e) throws IOException{
        System.out.println("SIGNUP");
        String username = signupUsernameTextField.getText();
        String password = signupPasswordTextField.getText();
        String password2 = signupConPasswordTextField.getText();

        String status = accountManager.signup(username, password, password2);

        switch (status) {
            case "pnomatch" -> warningSignup.setText("Password is not machted!");
            case "invalid" -> warningSignup.setText("Invalid Password");
            case "existed" -> warningSignup.setText("Name already existed");
            case "success" -> {
                navigateToLogin(e);
                warningSignup.setText("Sign up successfully!");
            }
            default -> {
            }
        }

//        System.out.println(username + password + password2);
    }
}
