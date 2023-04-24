package com.example.demo;

import backend.ChatroomManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField text_field;

    @FXML
    private Button createRoomButton;

    @FXML
    private Button joinRoomButton;

    @FXML
    private Button accountSettingButton;

    @FXML
    private ListView<String> roomsList;


    private int current_user_id;

    private String selected_room = null;

    private String username = null;

    ChatroomManager chatroomManager;

    String[] sample = {"room1","lolroom","heyyead"};

    public RoomController(int uid, String username){
        current_user_id = uid;
        chatroomManager = new ChatroomManager(uid);
        this.username = username;
//        System.out.println(chatroomManager.getRooms());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (current_user_id!=0){
            System.out.println(current_user_id);
            roomsList.getItems().addAll(chatroomManager.getRooms());

        }

        roomsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selected_room = roomsList.getSelectionModel().getSelectedItems().get(0);

            }
        });
    }

    public void createRoom(ActionEvent e){
        String room_name = text_field.getText();
        chatroomManager.create_room(room_name);
        roomsList.getItems().add(room_name);
    }

    public void joinRoom(ActionEvent e) throws IOException {
        System.out.println(selected_room);

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("conversation.fxml"));
//        Parent root = loader.load();
//
//        ConversationController conversationController = loader.getController();
//        conversationController.setRoomName(selected_room);

        int room_id = chatroomManager.join_room(selected_room);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("conversation.fxml"));
        loader.setControllerFactory(c -> new ConversationController(current_user_id, username, room_id, selected_room));

        Parent root = loader.load();


        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void navigateToAccountSetting(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("verify.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
