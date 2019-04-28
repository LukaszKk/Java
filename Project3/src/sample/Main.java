package sample;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application
{
    private Drawer drawer;
    static boolean enter = false;

    //=========================================================================================================//
    private void construct(Stage primaryStage)
    {
        drawer = new Drawer(primaryStage);
    }

    @Override
    public void start( Stage primaryStage )
    {
        construct(primaryStage);
        drawer.draw();
    }

    //=========================================================================================================//
    public static void main( String[] args )
    {
        launch(args);
    }
}
