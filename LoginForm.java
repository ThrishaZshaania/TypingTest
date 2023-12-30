package LoginForm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class LoginForm extends Application {

    private List<User> userDatabase = new ArrayList<>();
    private Stage primaryStage;
    private User currentUser;  // Declare a field to store the current user

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Type-A-Thon");

        GridPane gridPane = Start();
        Scene loginScene = new Scene(gridPane, 400, 250);

        this.primaryStage.setScene(loginScene);
        this.primaryStage.show();
    } 
//---------------------------------------------------------------------------------
    private GridPane Start() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label text1 = new Label("Type-A-Thon");
        Button startButton1 = new Button("Register");
        Button startButton2 = new Button("Login");


        gridPane.add(text1, 14, 4);
        gridPane.add(startButton1, 14, 6);
        gridPane.add(startButton2, 14, 7);

        startButton1.setOnAction(e -> handleStartButton1());
        startButton2.setOnAction(e -> handleStartButton2());


        return gridPane;
    }
    
    private GridPane createRegistrationLoginForm() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(10);

        Label regUsernameLabel = new Label("Username:");
        TextField regUsernameField = new TextField();
        Label regPasswordLabel = new Label("Password:");
        PasswordField regPasswordField = new PasswordField();
        Button registerButton = new Button("Register");

        gridPane.add(regUsernameLabel, 0, 0);
        gridPane.add(regUsernameField, 1, 0);
        gridPane.add(regPasswordLabel, 0, 1);
        gridPane.add(regPasswordField, 1, 1);
        gridPane.add(registerButton, 1, 2);

        registerButton.setOnAction(e -> handleRegistration(regUsernameField.getText(), regPasswordField.getText()));

        return gridPane;
    }
    
    private GridPane LoginForm() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(10);

        Label regUsernameLabel = new Label("Username:");
        TextField regUsernameField = new TextField();
        Label regPasswordLabel = new Label("Password:");
        PasswordField regPasswordField = new PasswordField();
        Button registerButton = new Button("Login");

        gridPane.add(regUsernameLabel, 0, 0);
        gridPane.add(regUsernameField, 1, 0);
        gridPane.add(regPasswordLabel, 0, 1);
        gridPane.add(regPasswordField, 1, 1);
        gridPane.add(registerButton, 1, 2);

        registerButton.setOnAction(e -> handleLogin(regUsernameField.getText(), regPasswordField.getText()));

        return gridPane;
    }
    
        private GridPane Main() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(50, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(50);

        Label Welcome = new Label("TYPE-A-THON");
        Button Button1 = new Button("Play");
        Button Button2 = new Button("Profile Player");
        Button Button3 = new Button("Leaderboard");
        Button Button4 = new Button("Exit");

        gridPane.add(Welcome, 2, 0);
        gridPane.add(Button1, 2, 2);
        gridPane.add(Button2, 2, 3);
        gridPane.add(Button3, 2, 4);
        gridPane.add(Button4, 2, 6);

        Button1.setOnAction(e -> handlePlay());
        Button2.setOnAction(e -> handleProfilePlayer());
        Button3.setOnAction(e -> handleLeaderboard());
        Button4.setOnAction(e -> Exit());

        return gridPane;
    }
 
    private GridPane Play() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(10);
        
        Label textplay = new Label("Choose your gamemode");
        Button Default= new Button("Default");
        Button Timed= new Button("Timed");
        Button Words= new Button("Words");
        Button Quote= new Button("Quote");

        gridPane.add(textplay, 0, 0);
        gridPane.add(Default, 2, 2);
        gridPane.add(Timed, 2, 3);
        gridPane.add(Words, 2, 4);
        gridPane.add(Quote, 2, 5);
        
//        Default.setOnAction(e -> DefaultGamemode());

        return gridPane;
    }
    
    private GridPane ProfilePlayer(User username) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(10);
        
    if (username != null) {
        // Player Profile Information
        Label usernameLabel = new Label("Player Profile for: " + username.getUsername());
        Label avgWPMLabel = new Label("Average WPM (All-time): " + username.getAverageWPM());
        Label avgAccuracyLabel = new Label("Average Accuracy (All-time): " + username.getAverageAccuracy());
        Label avgWPMLast10Label = new Label("Average WPM (Last 10 games): " + username.getAverageWPMLast10Games());
        Label avgAccuracyLast10Label = new Label("Average Accuracy (Last 10 games): " + username.getAverageAccuracyLast10Games());

        // Add labels to the GridPane
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(avgWPMLabel, 0, 1);
        gridPane.add(avgAccuracyLabel, 0, 2);
        gridPane.add(avgWPMLast10Label, 0, 3);
        gridPane.add(avgAccuracyLast10Label, 0, 4);
    } else {
        // Display a message if no user is logged in
        Label noUserLabel = new Label("No user logged in. Please log in first.");
        gridPane.add(noUserLabel, 0, 0);
    }
    
        Button Button1 = new Button("Main");
        gridPane.add(Button1, 2, 6);
        Button1.setOnAction(e -> GoToMain());

        return gridPane;
    }
    
    private GridPane Leaderboard() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setVgap(20);
        gridPane.setHgap(10);

        Label leaderboardLabel = new Label("Leaderboard");
        gridPane.add(leaderboardLabel, 0, 0);

        // Display header for the leaderboard
        Label usernameHeader = new Label("Username");
        Label wpmHeader = new Label("Average WPM (Last 10 games)");
        gridPane.add(usernameHeader, 0, 2);
        gridPane.add(wpmHeader, 1, 2);

        // Implement logic to retrieve and display other users in the leaderboard
        List<User> leaderboard = getLeaderboard();
        int row = 4;

        for (User user : leaderboard) {
            Button viewProfileButton = new Button(user.getUsername());
            Label wpmLabel = new Label(String.valueOf(user.getAverageWPMLast10Games()));
            gridPane.add(viewProfileButton, 0, row);
            gridPane.add(wpmLabel, 1, row);

            // to view the profile of the other user
            viewProfileButton.setOnAction(e -> handleViewProfile(user));
            row++;
        }

        // Add a button to go back to the main screen
        Button backButton = new Button("Main");
        backButton.setOnAction(e -> primaryStage.setScene(new Scene(Main(), 400, 350)));
        gridPane.add(backButton, 1, row + 1);

        return gridPane;
    }

    
//-------------------------------------------------------------------------------------
    
    private void handleStartButton1() {
        // Switch to the registration form when the Start button is clicked
        primaryStage.setScene(new Scene(createRegistrationLoginForm(), 400, 250));
    }    

    private void handleStartButton2() {
        // Switch to the login form when the Start button is clicked
        primaryStage.setScene(new Scene(LoginForm(), 400, 250));
    }  
        
    private void handleRegistration(String username, String password) {
        User newUser = new User(username, password);
        userDatabase.add(newUser);
        System.out.println("Registered user: " + username);
        // Switch back to the login form after registration
        primaryStage.setScene(new Scene(LoginForm(), 400, 250));
    }

    private void handleLogin(String username, String password) {
        for (User user : userDatabase) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful for user: " + username);
                currentUser=user;
            }else{ System.out.println("Login failed");
        }}
        primaryStage.setScene(new Scene(Main(), 400, 350));

    }
    
    private void handlePlay() {
        primaryStage.setScene(new Scene(Play(), 400, 350));
    } 
    
    
    private void handleProfilePlayer() {
        if (currentUser != null) {
            primaryStage.setScene(new Scene(ProfilePlayer(currentUser), 400, 350));
        } else {
            System.out.println("No user logged in.");  // Handle the case where no user is logged in
        }
    }        
    
    private void handleLeaderboard(){
        if (currentUser!=null){
            primaryStage.setScene(new Scene(Leaderboard(),400 ,250));

        } else{
            System.out.println("No user logged in.");
        }
    }
    
//    private void DefaultGamemode() {
//        Default defaultFrame = new Default();
//        }
    
    private void GoToMain() {
        primaryStage.setScene(new Scene(Main(), 400, 350));
    }
    
    private void Exit() {
        primaryStage.setScene(new Scene(createRegistrationLoginForm(), 400, 250));
    }    

    private void handleViewProfile(User otherUser) {
      primaryStage.setScene(new Scene(ProfilePlayer(otherUser), 400, 350));
    }
    
    private List<User> getLeaderboard() {
        // Copy the userDatabase to avoid modifying the original list
        List<User> usersCopy = new ArrayList<>(userDatabase);

        // Sort the usersCopy by average WPM (last 10 games) in descending order
        usersCopy.sort(Comparator.comparingDouble(User::getAverageWPMLast10Games).reversed());

        return usersCopy;
    }


    private static class User {
        private String username;
        private String password;
        private double averageWPM;
        private double averageAccuracy;
        private double[] last10GamesWPM = new double[10];
        private double[] last10GamesAccuracy = new double[10];
        private int gamesPlayed = 0;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public double getAverageWPM() {
            return averageWPM;
        }

        public void setAverageWPM(double averageWPM) {
            this.averageWPM = averageWPM;
        }

        public double getAverageAccuracy() {
            return averageAccuracy;
        }

        public void setAverageAccuracy(double averageAccuracy) {
            this.averageAccuracy = averageAccuracy;
        }

        public double getAverageWPMLast10Games() {
            return calculateAverage(last10GamesWPM);
        }

        public double getAverageAccuracyLast10Games() {
            return calculateAverage(last10GamesAccuracy);
        }

        public void addGameStatistics(double wpm, double accuracy) {
            if (gamesPlayed < 10) {
                last10GamesWPM[gamesPlayed] = wpm;
                last10GamesAccuracy[gamesPlayed] = accuracy;
            } else {
                // Shift the array to remove the oldest game statistics
                System.arraycopy(last10GamesWPM, 1, last10GamesWPM, 0, 9);
                System.arraycopy(last10GamesAccuracy, 1, last10GamesAccuracy, 0, 9);
                last10GamesWPM[9] = wpm;
                last10GamesAccuracy[9] = accuracy;
            }
            gamesPlayed++;
        }

        private double calculateAverage(double[] array) {
            double sum = 0;
            for (double value : array) {
                sum += value;
            }
            return (gamesPlayed > 0) ? sum / gamesPlayed : 0;
        }
    }
}