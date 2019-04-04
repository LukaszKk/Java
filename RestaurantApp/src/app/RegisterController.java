package app;

import connectivity.ConnectionClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterController extends Main
{
    public AnchorPane anchorPane;
    public Button signUpButton;
    public TextField userName;
    public TextField password;
    public ChoiceBox<String> choiceBox;
    public Hyperlink back;
    public Hyperlink exit;
    public Label loggedAs;

    /**
     * set initial properties
     * disable focusing cursor on TextField
     * different views if its first time run
     */
    public void initialize()
    {
        if( !Main.isFirstTimeRun )
        {
            back.setVisible(true);
            exit.setText( "Log out" );
            loggedAs.setVisible(true);
            loggedAs.setText( "Logged as: " + Main.loggedAs );
            choiceBox.getItems().add("Logistician");
            choiceBox.getItems().add("Waiter");
            choiceBox.getItems().add("Accountant");
        }
        else
        {
            back.setVisible(false);
            exit.setText( "Exit" );
            loggedAs.setVisible(false);
        }
        choiceBox.getItems().add("Manager");
        choiceBox.setValue("Manager");

        userName.setFocusTraversable(false);
        password.setFocusTraversable(false);
    }

    /**
     * Back button clicked
     * Close registration form
     * Open login form
     */
    public void backAction()
    {
        loadView( "workers" );
    }

    public void loadLogin()
    {
        loadView( "login" );
    }

    /**
     * load given view
     * @param view
     */
    private void loadView( String view )
    {
        Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
        try
        {
            Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/views/" + view + ".fxml"));
            Main.loadStage( fxmlLoader );
            primaryStage.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Exit button clicked
     * Close registration form
     */
    public void exitAction()
    {
        if( Main.isFirstTimeRun )
        {
            Stage registerStage = (Stage) anchorPane.getScene().getWindow();
            registerStage.close();
        }
        else
            loadLogin();
    }

    /**
     * Sign Up button clicked
     * Check if fields are filled
     * Open connection with databese
     * Check if there is user in databese with given userName
     * Show error or if everything is alright save new user in databese
     * Trigger backAction() to close registration form and open login form
     */
    public void signUpAction()
    {
        int checked = LoginController.checkFieldsFill( userName, password );
        if( checked != 2 )
            return;

        Connection connection = new ConnectionClass().getConnection();

        try
        {
            Statement statement = connection.createStatement();
            String sql = "SELECT name FROM users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while( resultSet.next() )
            {
                if( resultSet.getString(1).equals(userName.getText()) )
                {
                    userName.clear();
                    userName.setPromptText( "User name is already taken!");
                    userName.setStyle("-fx-prompt-text-fill: #ff0000");
                    password.clear();
                    connection.close();
                    return;
                }
            }
            sql = "INSERT INTO users VALUES('"+userName.getText()+"', '"+password.getText()+"', '"+choiceBox.getValue()+"');";
            statement.executeUpdate( sql );
            connection.close();
            if( !Main.isFirstTimeRun )
                backAction();
            else
            {
                Main.isFirstTimeRun = false;
                loadLogin();
            }
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }
}
