package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application
{
    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Open File");

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    static boolean fileOpen()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if( file != null )
        {
            pStage.setTitle(file.getName());
            Controller.readFile(file);
            return true;
        }

        return false;
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
