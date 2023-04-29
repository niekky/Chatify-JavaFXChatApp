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
        System.out.println("Welcome to Chatify!");
        System.out.println("Type /help to see options");

        ChatroomManager chatroomManager = new ChatroomManager(uid);


        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Chatify>> ");
            String user_input = scanner.nextLine();

            if (user_input.equals("/q")) {
                System.out.println("PROGRAM TERMINATED!");
                break;
            }

            System.out.println("#########################################################################");

            String[] parts = user_input.split(" ");
            switch (parts[0]) {
                case "/display_rooms" -> chatroomManager.displayRooms();
                case "/join" -> chatroomManager.join_room(parts[1]);
                case "/create" -> {
                    if (chatroomManager.create_room(parts[1])){
                        chatroomManager.join_room(parts[1]);
                    };
                }
                case "/history" -> chatroomManager.chat_history();
                case "/leave" -> chatroomManager.exit_room();
                case "/list" -> chatroomManager.list_user();
                case "/update" -> acc_manager.updateCLI();
                case "/logout" -> {
                    chatroomManager.logout();
                    int new_uid = accountScope();
                    chatroomManager = new ChatroomManager(new_uid);
                }
                case "/help" -> {
                    System.out.println("Help: ");
                    System.out.println("/display_rooms      : Display all available chatrooms");
                    System.out.println("/join <room_name>   : Join a chatroom");
                    System.out.println("/create <room_name> : Create a chatroom");
                    System.out.println("/history            : Show all the past messages");
                    System.out.println("/leave              : Leave a current chatroom");
                    System.out.println("/list               : List all users in your room");
                    System.out.println("/update             : Update user setting");
                    System.out.println("/logout             : Logout");
                    System.out.println("/q                  : Exit the program");
                }
                case "" -> System.out.println("Invalid Input");
                default -> {
                    if (chatroomManager.inRoom() && parts[0].charAt(0)!='/'){
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

            System.out.println("#########################################################################");

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
        int uid = -1;
        while (uid == -1){
            uid = accountScope();
        }
        chatroomScope(uid);

    }




}