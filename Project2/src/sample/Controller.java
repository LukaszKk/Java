package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
    private double[] dataValues;
    private static boolean isLin = true;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Menu profile;

    @FXML
    private LineChart<Number, Number> lineChart;

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
    private Menu display;

    @FXML
    private MenuItem format;

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
                display.setDisable(false);
                format.setText("Logaritmic");
                isLin = true;

                MenuItem[] profileItems = new MenuItem[profileNames.length];
                for( int i = 0; i < profileNames.length; i++ )
                {
                    profileItems[i] = new MenuItem(profileNames[i]);
                    int finalI = i;

                    profileItems[i].setOnAction(actionEvent ->
                    {
                        generateData(finalI);
                    });

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
        display.setDisable( true );

        scrollPane.setPannable( true );
        lineChart.prefWidthProperty().bind(borderPane.widthProperty().multiply(0.9));
        lineChart.prefHeightProperty().bind(borderPane.heightProperty().multiply(3));

        lineChart.setVisible( false );
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
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
        xAxis.setLabel(profileNames[number]);

        isLin = true;

        if( haveNegative() )
            display.setDisable(true);
        else
            display.setDisable(false);


        drawChart();
    }

    @SuppressWarnings("unchecked")
    private void drawChart()
    {
        lineChart.getData().clear();
        LineChart.Series<Number, Number> series = new LineChart.Series<>();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        double yAxisCount = (double) dataValues.length/16;
        double xAxisCount = (double) dataValues.length/64;

        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(-fourDoubles[0]);
        yAxis.setLowerBound(-fourDoubles[1]);
        yAxis.setTickUnit((fourDoubles[1]-fourDoubles[0])/yAxisCount);
        yAxis.setMinorTickVisible(true);

        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number value) {
                return String.format("%7.1f", -value.doubleValue());
            }
        });

        for( int i = 0; i < dataValues.length; i++ )
        {
            if( dataValues[i] == fourDoubles[3] )
                continue;

            if( min > dataValues[i] && dataValues[i] != fourDoubles[3] )
                min = dataValues[i];

            if( max < dataValues[i] )
                max = dataValues[i];

            if( isLin )
                series.getData().add( new LineChart.Data<>(dataValues[i], -(fourDoubles[0] + i*fourDoubles[2])) );
            else
                series.getData().add( new LineChart.Data<>(Math.log10(dataValues[i]), -(fourDoubles[0] + i*fourDoubles[2])) );
        }

        lineChart.getData().addAll(series);

        xAxis.setAutoRanging(false);
        if( isLin )
        {
            xAxis.setLowerBound(min);
            xAxis.setUpperBound(max);
            xAxis.setTickUnit((max-min)/xAxisCount);
        }
        else
        {
            xAxis.setLowerBound(Math.log10(min));
            xAxis.setUpperBound(Math.log10(max));
            xAxis.setTickUnit((Math.log10(max)-Math.log10(min))/xAxisCount);
        }
        xAxis.setMinorTickVisible(true);

        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setVerticalGridLinesVisible(true);

        if( isLin )
            format.setText("Logaritmic");
        else
            format.setText("Linear");

        xAxis.setLabel( xAxis.getLabel().split(" ")[0] + (isLin ? " Linear" : " Logaritmic"));
    }

    @FXML
    void changeFormat()
    {
        isLin = !isLin;
        drawChart();
    }

    private boolean haveNegative()
    {
        for( double d : dataValues )
        {
            if( (d <= 0) && (d != fourDoubles[3]) )
            {
                return true;
            }
        }

        return false;
    }
}