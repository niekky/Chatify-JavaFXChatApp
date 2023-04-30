# Chatify
A basic chatting application made completely in Java. Chatify is a full-stack application, where front-end is developed with JavaFX framework and back-end is made to leverage database management by JDBC. Chatify's database is done on PostgreSQL and hosted with AWS.

Features in Chatify:
- Supports both CLI and GUI
- Login/Signup and Account Setting
- Display all available chat rooms
- Join/Create a room
- Display all available users in one specific room
- Real-time conversation
- Encrypted password for security

## Setup
- Clone this project repository
- Create .env file in the demo folder (main folder of the project workspace) containing:
    - "url": your hosted database service domain
    - "user": your username in your hosted database server
    - "pass": your password in your hosted database server
- To run Chatify, go to src/main/ 
    - For GUI: Execute the file "LoginApplication.java" in "com.example.demo" package.
    - For CLI: Execute the file "Main.java" in "backend" package.

## Database Design
![](https://i.imgur.com/VLPCdFO.png)


## Libraries Requirement
- JDBC
- Dotenv

## Credit
Developed by 
- Nick Nguyen : tnguy231@asu.edu
- Cole Kemp : cbkemp@asu.edu
Chatify was developed as a final project for our CSE205 class at Arizona State University.
