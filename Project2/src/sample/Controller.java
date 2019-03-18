package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.util.Arrays;

public class Controller
{
    private static boolean flagOneOpen = false;
    private static double[] fourDoubles;        //START, STOP, STEP, NULL
    private static String[] profileNames;
    private MenuItem[] profileItems;
    private double[] dataValues;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Menu profile;

    @FXML
    private LineChart lineChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Menu file;

    @FXML
    private MenuItem open;

    @FXML
    void fileOpen(Event event)
    {
        if( !flagOneOpen )
        {
            flagOneOpen = true;
            file.setDisable(true);
            if( Main.fileOpen() )
            {
                profile.setDisable(false);
                profile.getItems().clear();

                profileItems = new MenuItem[profileNames.length];
                for( int i = 0; i < profileNames.length; i++ )
                {
                    profileItems[i] = new MenuItem(profileNames[i]);
                    int finalI = i;
                    profileItems[i].setOnAction(actionEvent -> generateData(finalI));
                    profile.getItems().add(profileItems[i]);
                }
            }
            flagOneOpen = false;
            file.setDisable(false);
        }
    }

    @FXML
    void initialize()
    {
        profile.setDisable( true );

        lineChart.setVisible( false );

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.Y_AXIS);
        yAxis.setAutoRanging( false );
        yAxis.setLowerBound( 100 );
        yAxis.setUpperBound( 0 );
    }

    static void readFile( File file )
    {
        FileReaderer fileReaderer = new FileReaderer();
        fourDoubles = fileReaderer.readFour( file );
        profileNames = fileReaderer.readProfileNames( file );
    }

    private void generateData( int number )
    {
        FileReaderer fileReaderer = new FileReaderer();
        dataValues = fileReaderer.readData( number );
        lineChart.setVisible(true);
        drawChart();
    }

    private void drawChart()
    {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for( int i = 0; i < dataValues.length; i++ )
        {
            if( min > dataValues[i] && dataValues[i] != fourDoubles[3] )
                min = dataValues[i];

            if( max < dataValues[i] )
                max = dataValues[i];

            //TODO
            series.getData().add(new XYChart.Data<>(fourDoubles[0] + i*fourDoubles[2] + "", dataValues[i] == fourDoubles[3] ? 0 : dataValues[i]));
        }

        yAxis.setLowerBound(max + 1000);
        yAxis.setUpperBound(min - 1000);



        lineChart.getData().add(series);
    }
}
