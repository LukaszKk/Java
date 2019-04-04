package app;

import connectivity.ConnectionClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application
{
    static boolean isFirstTimeRun;
    static String loggedAs;

    /**
     * load starting stage depending on if its first time run app
     * @param stage
     * @throws IOException
     */
    private void loadStartingStage(Stage stage) throws IOException
    {
        Parent root;
        isFirstTimeRun = isFirstTime();
        if( isFirstTimeRun )
            root = FXMLLoader.load( getClass().getResource("/views/register.fxml"));
        else
            root = FXMLLoader.load( getClass().getResource("/views/login.fxml"));

        stage.setTitle("Welcome");
        stage.setScene(new Scene(root, 600, 400));
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * opens given form
     * @param fxmlLoader
     */
    static void loadStage( Parent fxmlLoader )
    {
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * initial method
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage)
    {
        ConnectionClass.createDB();
        ConnectionClass.createTables();

        try
        {
            loadStartingStage( primaryStage );
        } catch( IOException e )
        {
            System.out.println( e.getMessage() );
        }
    }

    /**
     * Checks if exists any manager
     * If not - application was run first time
     */
    private static boolean isFirstTime()
    {
        Connection connection = new ConnectionClass().getConnection();
        try
        {
            Statement statement = connection.createStatement();
            String sql = "SELECT position FROM users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while( resultSet.next() )
                if( resultSet.getString(1).equals("Manager") )
                    return false;
            connection.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
