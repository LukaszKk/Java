package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.io.*;

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
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Menu file;

    @FXML
    private MenuItem open;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void fileOpen(Event event)
    {
        if( !flagOneOpen )
        {
            flagOneOpen = true;
            file.setDisable(true);
            if( Main.fileOpen() )
            {
                lineChart.getData().clear();
                generateData( 0 );

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

        scrollPane.setPannable(true);
        lineChart.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.9));
        lineChart.prefHeightProperty().bind(borderPane.heightProperty().multiply(1.5));

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
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for( int i = 0; i < dataValues.length; i++ )
        {
            if( dataValues[i] == fourDoubles[3] )
                continue;

            if( min > dataValues[i] && dataValues[i] != fourDoubles[3] )
                min = dataValues[i];

            if( max < dataValues[i] )
                max = dataValues[i];

            series.getData().add(new XYChart.Data<>(dataValues[i], fourDoubles[0] + i*fourDoubles[2]));
        }

        yAxis.setUpperBound(fourDoubles[0]);
        yAxis.setLowerBound(fourDoubles[1]);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound( min - min/10);
        xAxis.setUpperBound( max + max/10);

        lineChart.getData().add(series);
    }
}