package backend;

import backend.AccountManager;
import backend.ChatroomManager;

import java.util.Scanner;
import java.util.*;

public class Main {
    public static AccountManager acc_manager = new AccountManager();
    private static void argsLog(String[] parts){
        for (String i: parts){
            System.out.print(i+" ");
        }
        System.out.println();
    }


    private static void chatroomScope(int uid) {
        System.out.println("Chatroom WIP");

        ChatroomManager chatroomManager = new ChatroomManager(uid);


        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter: ");
            String user_input = scanner.nextLine();

            if (user_input.equals("/q")) {
                System.out.println("PROGRAM TERMINATED!");
                break;
            }


            String[] parts = user_input.split(" ");
//            argsLog(parts);
            switch (parts[0]) {
                case "/display_rooms" -> chatroomManager.displayRooms();
                case "/join" -> chatroomManager.join_room(parts[1]);
                case "/create" -> chatroomManager.create_room(parts[1]);
                case "/chat_history" -> chatroomManager.chat_history();
                case "/exit_room" -> chatroomManager.exit_room();
                case "/list_users" -> chatroomManager.list_user();
                case "/update" -> acc_manager.updateCLI();
//                case "/c" -> {
//                    String message = "";
//                    for (int i = 1; i < parts.length; i++) {
//                        message = message.concat(parts[i]).concat(" ");
//                    }
//                    chatroomManager.send_message(message.trim());
//                }
                case "/help" -> {
                    System.out.println("Help: ");
                    System.out.println("/display_rooms      : Display all public chatrooms");
                    System.out.println("/join <room_name>   : Join a certain chatroom");
                    System.out.println("/create <room_name> : Create a certain chatroom");
                    System.out.println("/exit_room          : Join a certain chatroom");
//                    System.out.println("/c <text_message>   : Chat");
                    System.out.println("/list_users         : List all users in your room");
                    System.out.println("/update             : Update User Option");
                    System.out.println("/exit_room          : Exit your chatroom");
                    System.out.println("/q                  : Exit the program");
                }
                default -> {
                    if (chatroomManager.inRoom()){
                        String message = "";
                        for (String part : parts) {
                            message = message.concat(part).concat(" ");
                        }
                        chatroomManager.send_message(message.trim());
                    } else {
                        System.out.println("Invalid Command!");
                    }
                }
            }
        }
    }

    private static int accountScope(){
        System.out.println("Enter and press: ");
        System.out.println("'l' to login");
        System.out.println("'s' to signup");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter: ");
        String user_input = scanner.nextLine();
        switch (user_input) {
            case "l" -> {
                return acc_manager.loginCLI();
            }
            case "s" -> {
                acc_manager.signupCLI();
                return acc_manager.loginCLI();
            }
            default -> System.out.println("Invalid Command!");
        }
        return -1;
    }

    public static void main(String[] args) {

        //m.connectToDatabase("projectdb");
        //System.out.println(m.searchForMatch("users", "username", "coles"));
//    	m.signup();
//    	m.login();
        //UPDATE my_table SET my_column = 'new_value' WHERE primary_key_column = 'primary_key_value';
        //m.updateColumn("users", "username", "coles" , "user_id", "1");
        //m.deleteData("users");
//        chatroomScope();
        int uid = -1;
        while (uid == -1){
            uid = accountScope();
        }
        chatroomScope(uid);

    }




}