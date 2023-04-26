package com.example.demo;

import backend.ChatroomManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ConversationController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int current_user_id = -1;

    private int room_id;

    @FXML
    private TextField text_field;

    @FXML
    private Button sendButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> conversationList;

    @FXML
    private ListView<String> membersList;

    @FXML
    private Label chatroomName;

    private String username = null;
    private String room_name = null;

    static ChatroomManager chatroomManager;

    String[] sample_messages = {"user1: Hi","user2: Hey","user1: How's going?"};

    String[] sample_members = {"user1", "user2"};
    TimerTask task = new TimerTask();
    Timer timer = new Timer();


    private ObservableList<String> observableMessages = FXCollections.observableArrayList();

    public ConversationController(int uid, String username, int room_id, String room_name){
        current_user_id = uid;
        this.room_id = room_id;
        this.username = username;
        this.room_name = room_name;
        chatroomManager = new ChatroomManager(uid);
        chatroomManager.setRoomID(room_id);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        conversationList.getItems().addAll(sample_messages);
//        membersList.getItems().addAll(sample_members);
//        List<String> messages = chatroomManager.getConversation();
//        conversationList.getItems().addAll(messages);
        timer.scheduleAtFixedRate(task, 1000, 1000);

        chatroomName.setText(room_name);

        membersList.getItems().addAll(chatroomManager.getUserList());

        observableMessages.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("Invalidated");
            }
        });

        conversationList.setItems(observableMessages);

    }

    public void sendChat(ActionEvent e){
        String text = text_field.getText();
        String full = String.format("%s: %s", username, text);
        conversationList.getItems().add(full);
        chatroomManager.send_message(text);
        System.out.println("User said: " + text);
    }

    public void goBack(ActionEvent e) throws IOException {

//        Parent root = FXMLLoader.load(getClass().getResource("rooms.fxml"));
//        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
        timer.cancel();
        task.cancel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rooms.fxml"));
        loader.setControllerFactory(c -> new RoomController(current_user_id, username));


        Parent root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

     class TimerTask extends java.util.TimerTask{
        int timer = 0;
        @Override
        public void run() {
            Platform.runLater(() -> {
                System.out.println(timer);
                if (timer <= 0){
                    List<String> messages = chatroomManager.getConversation();
                    System.out.println(messages);
                    observableMessages.setAll(messages);
                    timer = 5;
                }
                timer--;
            });
        }
    }

}
