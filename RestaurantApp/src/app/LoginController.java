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


public class LoginController extends Main
{
    public AnchorPane anchorPane;
    public TextField loginField;
    public PasswordField passwordField;
    public Button signInButton;
    public Label loginLabel;
    public Hyperlink exitButton;

    /**
     * set fields initial property
     * disable focusing cursor on TextField
     */
    public void initialize()
    {
        loginField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
    }

    /**
     * checks if fields were filed correctly
     * else prints a message
     * @param field
     * @param password
     * @return
     */
    static int checkFieldsFill( TextField field, TextField password )
    {
        int checked = 2;
        if( field.getText().length() == 0 )
        {
            field.setPromptText( "User name must be filled!");
            field.setStyle("-fx-prompt-text-fill: #ff0000");
            password.clear();
            --checked;
        }

        if( password.getText().length() == 0 )
        {
            password.setPromptText( "Password must be filled!");
            password.setStyle("-fx-prompt-text-fill: #ff0000");
            --checked;
        }

        return checked;
    }

    public void exitAction()
    {
        Stage registerStage = (Stage) anchorPane.getScene().getWindow();
        registerStage.close();
    }

    /**
     * Sign In button clicked
     * Check if fields are filled
     * Open connection with databese
     * Give an error or if everything is alrigth check if given user exists in database
     */
    public void signInButtonAction()
    {
        int checked = checkFieldsFill( loginField, passwordField );
        if( checked != 2 )
            return;

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try
        {
            Statement statement = connection.createStatement();
            String sql = "SELECT name FROM users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while( resultSet.next() )
            {
                if( resultSet.getString(1).equals(loginField.getText()) )
                {
                    --checked;
                    break;
                }
            }

            sql = "SELECT password FROM users;";
            resultSet = statement.executeQuery(sql);
            while( resultSet.next() )
            {
                if( resultSet.getString(1).equals(passwordField.getText()) )
                {
                    --checked;
                    sql = "SELECT position FROM users WHERE password = '" + passwordField.getText() + "';";
                    break;
                }
            }

            if( checked == 0 )
            {
                Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
                resultSet = statement.executeQuery(sql);
                resultSet.next();
                sql = resultSet.getString(1);

                String openViewName = null;
                switch( sql )
                {
                    case "Accountant":
                        Main.loggedAs = "Accountant";
                        //TODO... replace "/views/login.fxml"
                            openViewName = "/views/login.fxml";
                        break;
                    case "Logistician":
                        Main.loggedAs = "Logistician";
                        //TODO... replace "/views/login.fxml"
                            openViewName = "/views/login.fxml";
                        break;
                    case "Waiter":
                        Main.loggedAs = "Waiter";
                        //TODO... replace "/views/login.fxml"
                            openViewName = "/views/login.fxml";
                        break;
                    case "Manager":
                        Main.loggedAs = "Manager";
                            openViewName = "/views/manager.fxml";
                        break;
                    default:
                        openViewName = "/views/login.fxml";
                        break;
                }

                Parent fxmlLoader = FXMLLoader.load(getClass().getResource(openViewName));
                Main.loadStage( fxmlLoader );

                primaryStage.close();
            }
            else
            {
                passwordField.clear();
                passwordField.setPromptText( "Invalid user name or password!");
                passwordField.setStyle("-fx-prompt-text-fill: #ff0000");
            }
            connection.close();
        }
        catch( SQLException | IOException e )
        {
            e.printStackTrace();
        }
    }
}
