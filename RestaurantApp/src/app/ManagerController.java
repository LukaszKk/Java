package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerController
{
    public AnchorPane anchorPane;
    public Hyperlink logOutButton;
    public Button workersButton;
    public Label loggedAs;

    public void initialize()
    {
        loggedAs.setText( "Logged as: " + Main.loggedAs );
    }

    public void workersClicked()
    {
        loadView( "workers");
    }

    public void logOutClicked()
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
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        primaryStage.close();
    }
}