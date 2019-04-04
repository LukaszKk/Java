package app;

import connectivity.ConnectionClass;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WorkersController
{
    public AnchorPane anchorPane;
    public Hyperlink backButton;
    public Button createAccountButton;
    public Hyperlink logOutButton;
    public Label loggedAs;
    public ListView listView;

    public void initialize()
    {
        loggedAs.setText("Logged as: " + Main.loggedAs);
        listWorkers();
    }

    public void backAction()
    {
        loadView("manager");
    }

    public void logOutAction()
    {
        loadView("login");
    }

    public void createAccountAction()
    {
        loadView("register");
    }

    /**
     * load given view
     *
     * @param view
     */
    private void loadView(String view)
    {
        try
        {
            Stage primaryStage = (Stage) anchorPane.getScene().getWindow();

            Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/views/" + view + ".fxml"));
            Main.loadStage(fxmlLoader);
            primaryStage.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * list all workers
     */
    private void listWorkers()
    {
        ArrayList<String> name = getWorkersInfo( "name" );
        ArrayList<String> position = getWorkersInfo( "position" );

        for( int i = 0; i < name.size(); i++ )
        {
            Hyperlink hp = new Hyperlink( "Name: \"" + name.get(i) + "\" Position: " + position.get(i) );
            int finalI = i;
            hp.setOnAction(actionEvent ->
            {
                EditWorkersController.userNameDB = name.get(finalI);
                loadView( "editWorkers" );
            } );
            listView.getItems().add( hp );
        }
    }

    private ArrayList<String> getWorkersInfo( String attribute )
    {
        ArrayList<String> result = new ArrayList<>();
        Connection connection = new ConnectionClass().getConnection();
        try
        {
            Statement statement = connection.createStatement();
            String sql = "SELECT " + attribute + " FROM users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while( resultSet.next() )
                result.add( resultSet.getString(1) );
            connection.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }

        return result;
    }
}
