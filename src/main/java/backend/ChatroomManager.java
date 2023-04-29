package backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatroomManager extends SQLManager{
    private int current_room_id = -1;
    private int user_id = -1;

    public ChatroomManager(int user_id){
        this.user_id = user_id;
    }

    public void setRoomID(int roomID){
        current_room_id = roomID;
    }

    public void displayRooms(){
        System.out.println("All rooms available: ");

        try {
            Connection connection = connectDatabase();

            Statement stmt = connection.createStatement();

            String q1 = "SELECT * FROM rooms";
            ResultSet rs = stmt.executeQuery(q1);

            while (rs.next()){
                System.out.printf("%s: %s\n",rs.getString(1), rs.getString(2));
            }
            System.out.println();
            rs.close();
            stmt.close();
            connection.close();

        } catch (Exception e){
            System.out.println("Query Error");
        }
    }

    public List<String> getRooms(){
        List<String> rooms = new ArrayList<>();
        try {
            Connection connection = connectDatabase();

            Statement stmt = connection.createStatement();

            String q1 = "SELECT * FROM rooms";
            ResultSet rs = stmt.executeQuery(q1);

            while (rs.next()){
                System.out.printf("%s: %s\n",rs.getString(1), rs.getString(2));
                rooms.add(rs.getString(2));
            }

            rs.close();
            stmt.close();
            connection.close();
            return rooms;
        } catch (Exception e){
            System.out.println("Query Error");
            return null;
        }
    }

    public void logout(){
        this.user_id = -1;
        this.current_room_id = -1;
        System.out.println("Logout Successfully!");
    }

    public int join_room(String room_name){

        if (inRoom()){
            System.out.println("You are already in the chat");
            return -1;
        }

        try {
            Connection connection = connectDatabase();

            Statement stmt = connection.createStatement();

            String q1 = "SELECT * FROM rooms WHERE rooms.room_name = '"+room_name+"';";
            ResultSet rs = stmt.executeQuery(q1);

            if (!rs.next()){
                System.out.println("Invalid room!");
            } else {
                int room_id = rs.getInt(1);
                String name = rs.getString(2);

                String sql_check_dup = String.format(
                        "SELECT * FROM users_room " +
                        "WHERE users_room.user_id = %d AND users_room.room_id = %d;",
                        user_id,
                        room_id
                );

                ResultSet rs_dup = stmt.executeQuery(sql_check_dup);
                if (!rs_dup.next()){
                    String sql_insert = String.format(
                            "INSERT INTO users_room(user_id, room_id) " +
                                    "VALUES (%d, %d);",
                            user_id,
                            room_id
                    );
                    stmt.executeUpdate(sql_insert);
                }
                rs_dup.close();
                current_room_id = room_id;
                System.out.printf("Welcome to %s!\n",name);
            }
            rs.close();
            stmt.close();
            connection.close();
            return current_room_id;

        } catch (Exception e){
            System.out.println("ERROR: " + e);
            return -1;
        }
    }

    public boolean create_room(String room_name){
        if (inRoom()){
            System.out.println("You are already in the chat");
            return false;
        }
        for(int i = 0; i<room_name.length();i++){
            char ch = room_name.charAt(i);
            if(!(Character.isDigit(ch)||Character.isLowerCase(ch))){
                System.out.println("You are only allowed to use lowercase letters and numbers!");
                return false;
            }
        }

        try {
            Connection connection = connectDatabase();

            Statement stmt = connection.createStatement();

            if (searchForMatch("rooms","rooms.room_name",room_name)){
                System.out.println("Room name already existed! Please use different name!");
                return false;
            } else {

                int newKey = maxVal("rooms", "room_id") + 1;
                String sql_insert_room = String.format(
                        "INSERT INTO rooms(room_id, room_name) " +
                        "VALUES (%d, '%s');",
                        newKey,
                        room_name
                );
                stmt.executeUpdate(sql_insert_room);
                System.out.printf("%s Created!\n", room_name);

            }
            System.out.println();
            stmt.close();
            connection.close();
            return true;
        } catch (Exception e){
            System.out.println("ERROR: " + e);
            return false;
        }
    }

    public boolean inRoom(){
        if (current_room_id != -1){
            return true;
        } else return false;
    }

    public void exit_room(){
        if (inRoom()){
            current_room_id = -1;
            System.out.println("Room Exited");
            System.out.println();
        }
    }

    public void chat_history(){
        if (inRoom()){
            try {
                Connection connection = connectDatabase();

                Statement stmt = connection.createStatement();

                String sql_chat_query = "SELECT users.username, conversation.message_text " +
                                        "FROM users, conversation " +
                                        "WHERE users.user_id = conversation.user_id AND conversation.room_id = "+current_room_id+";";

                ResultSet rs = stmt.executeQuery(sql_chat_query);

                while (rs.next()){
                    System.out.printf("%s: %s\n", rs.getString(1), rs.getString(2) );
                }

                System.out.println();
                rs.close();
                stmt.close();
                connection.close();

            } catch (Exception e){
                System.out.println("ERROR: " + e);
            }
        } else {
            System.out.println("You are not in a chatroom!");
            System.out.println();
        }
    }

    public List<String> getConversation(){
        List<String> conversation = new ArrayList<>();
        if (inRoom()){
            try {
                Connection connection = connectDatabase();

                Statement stmt = connection.createStatement();

                String sql_chat_query = "SELECT users.username, conversation.message_text " +
                        "FROM users, conversation " +
                        "WHERE users.user_id = conversation.user_id AND conversation.room_id = "+current_room_id+";";

                ResultSet rs = stmt.executeQuery(sql_chat_query);

                while (rs.next()){
                    conversation.add(String.format("%s: %s", rs.getString(1), rs.getString(2)));
                }

                rs.close();
                stmt.close();
                connection.close();
                return conversation;
            } catch (Exception e){
                System.out.println("ERROR: " + e);
                return null;
            }
        } else {
            System.out.println("You are not in a chatroom!");
            System.out.println();
            return null;
        }
    }

    public void list_user(){
        if (inRoom()){
            try {
                Connection connection = connectDatabase();

                Statement stmt = connection.createStatement();

                String sql_users_query = String.format(
                        "SELECT users.username " +
                        "FROM users, users_room " +
                        "WHERE users.user_id = users_room.user_id AND users_room.room_id = %d;",
                        current_room_id
                );

                ResultSet rs = stmt.executeQuery(sql_users_query);

                System.out.println("Players: ");
                while (rs.next()){
                    System.out.println(rs.getString(1));
                }

                System.out.println();
                rs.close();
                stmt.close();
                connection.close();

            } catch (Exception e){
                System.out.println("ERROR: " + e);
            }
        } else {
            System.out.println("You are not in a chatroom!");
            System.out.println();
        }
    }

    public List<String> getUserList(){
        List<String> users = new ArrayList<>();
        if (inRoom()){
            try {
                Connection connection = connectDatabase();

                Statement stmt = connection.createStatement();

                String sql_users_query = String.format(
                        "SELECT users.username " +
                                "FROM users, users_room " +
                                "WHERE users.user_id = users_room.user_id AND users_room.room_id = %d;",
                        current_room_id
                );

                ResultSet rs = stmt.executeQuery(sql_users_query);

                System.out.println("Players: ");
                while (rs.next()){
                    users.add(rs.getString(1));
                }

                rs.close();
                stmt.close();
                connection.close();
                return users;
            } catch (Exception e){
                System.out.println("ERROR: " + e);
                return null;
            }
        } else {
            System.out.println("You are not in a chatroom!");
            System.out.println();
            return null;
        }
    }

    public void send_message(String text){
        if (inRoom()){
            try {
                Connection connection = connectDatabase();

                Statement stmt = connection.createStatement();

                String sql_send_message = String.format(
                        "INSERT INTO conversation(message_text, room_id, user_id) " +
                                "VALUES ('%s', %d, %d)",
                        text,
                        current_room_id,
                        user_id
                        );
                stmt.executeUpdate(sql_send_message);
                System.out.printf("You said %s\n", text);
                System.out.println();
                stmt.close();
                connection.close();

            } catch (Exception e){
                System.out.println("ERROR: " + e);
            }
        } else {
            System.out.println("You are not in a chatroom!");
            System.out.println();
        }
    }
}
