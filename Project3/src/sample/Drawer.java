package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

class Drawer
{
    private Stage primaryStage;
    private Pair<Double, Double> initialTouch;
    private MutablePair<Integer, Integer> firstPoint;
    private Pair<Double, Double> anglePoint;
    private Pair<Double, Double> endingTouch;
    private Canvas layer;
    private double xNewEnd;
    private double yNewEnd;
    private ArrayList<MutablePair<Integer, Integer>> nodes;

    //=========================================================================================================//
    Drawer( Stage primaryStage )
    {
        this.primaryStage = primaryStage;
        firstPoint = new MutablePair<>(0, 0);
        initialTouch = new Pair<>(0d, 0d);
        anglePoint = new Pair<>(0d, 0d);
        endingTouch = new Pair<>(0d, 0d);
        layer = new Canvas();
        xNewEnd = 0;
        yNewEnd = 0;
        nodes = new ArrayList<>();
    }

    //=========================================================================================================//
    void draw()
    {
        Pane pane = new Pane();
        StackPane root = new StackPane();
        root.setMaxSize( 300, 300 );

        Scene scene = new Scene(pane, 400, 400);

        Canvas canvas = new Canvas(400, 400);
        Calculator calculator = new Calculator(pane, canvas);

        pane.getChildren().add(root);
        primaryStage.setScene(scene);

        var ref = new Object()
        {
            int first = 0;
        };

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
        {
            if( ref.first == 0 )
            {
                ref.first = 1;
                anglePoint = new Pair<>(event.getSceneX()-10, event.getSceneY());
                firstPoint = new MutablePair<>((int) event.getSceneX(), (int) event.getSceneY());
                nodes.add( firstPoint );
                mousePressed(root, event.getSceneX(), event.getSceneY());
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event ->
        {
            if( !Main.enter )
            {
                GraphicsContext context = layer.getGraphicsContext2D();
                context.clearRect(0, 0, layer.getWidth(), layer.getHeight());
                context.strokeLine(initialTouch.getKey(), initialTouch.getValue(), event.getSceneX(), event.getSceneY());
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event ->
        {
            if( !Main.enter )
            {
                endingTouch = new Pair<>(event.getSceneX(), event.getSceneY());
                makeLineStraight(event, root);
            }
        });

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent ->
        {
            if( keyEvent.getCode() == KeyCode.ENTER && !Main.enter )
            {
                Main.enter = true;
                drawEndingLine();
                calculator.setNodes( nodes );
                calculator.setEdge();
                calculator.calc();
            }
        });

        root.getChildren().add(canvas);
        primaryStage.show();
    }

    //=========================================================================================================//
    private void initDraw( GraphicsContext gc )
    {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        gc.fill();
        gc.strokeRect(
                0,               //xNewEnd of the upper left corner
                0,              //yNewEnd of the upper left corner
                canvasWidth,        //width of the rectangle
                canvasHeight);      //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
    }

    //=========================================================================================================//
    private void mousePressed( StackPane root, double x, double y )
    {
        Canvas newLayer = new Canvas(400, 400);
        GraphicsContext context = newLayer.getGraphicsContext2D();
        initDraw(context);

        layer = newLayer;
        root.getChildren().add(0, newLayer);
        initialTouch = new Pair<>(x, y);
    }

    //=========================================================================================================//
    private double getAngle( double x, double y )
    {
        return Math.atan2(y - initialTouch.getValue(),
                x - initialTouch.getKey()) -
                Math.atan2(anglePoint.getValue() - initialTouch.getValue(),
                        anglePoint.getKey() - initialTouch.getKey());
    }

    private int length( Pair<Double, Double> p1, Pair<Double, Double> p2 )
    {
        return (int) Math.sqrt(Math.pow(p2.getKey() - p1.getKey(),2) + Math.pow(p2.getValue() - p1.getValue(),2));
    }

    //=========================================================================================================//
    private void makeLineStraight(MouseEvent event, StackPane root)
    {
        xNewEnd = event.getSceneX();
        yNewEnd = event.getSceneY();
        double degree = Math.toDegrees(getAngle(xNewEnd, yNewEnd));

        int len = length(initialTouch, endingTouch);
        if( (degree >= -45 && degree < 45) || (degree >= 315) || (degree < -315) )
        {
            xNewEnd = initialTouch.getKey()-len;
            yNewEnd = initialTouch.getValue();
        } else if( (degree >= 45 && degree < 135) || (degree >= -315 && degree < -225) )
        {
            yNewEnd = initialTouch.getValue()-len;
            xNewEnd = initialTouch.getKey();
        } else if( (degree >= 135 && degree < 225) || (degree >= -225 && degree < -135) )
        {
            xNewEnd = initialTouch.getKey()+len;
            yNewEnd = initialTouch.getValue();
        } //else if( (degree >= 225 && degree < 315) || (degree >= -135 && degree < -45) )
        else
        {
            yNewEnd = initialTouch.getValue()+len;
            xNewEnd = initialTouch.getKey();
        }

        GraphicsContext context = layer.getGraphicsContext2D();
        context.clearRect(0, 0, layer.getWidth(), layer.getHeight());
        context.strokeLine(initialTouch.getKey(), initialTouch.getValue(), xNewEnd, yNewEnd);

        anglePoint = new Pair<>(xNewEnd -10, yNewEnd);

        nodes.add( new MutablePair<>((int) xNewEnd, (int) yNewEnd) );

        mousePressed(root, xNewEnd, yNewEnd);
    }

    //=========================================================================================================//
    private void drawEndingLine()
    {
        GraphicsContext context = layer.getGraphicsContext2D();
        context.clearRect(0, 0, layer.getWidth(), layer.getHeight());
        if( nodes.size() <= 4 )
        {
            if( !firstPoint.getValue().equals(nodes.get(1).getValue()) && nodes.size() != 4 )
            {
                context.strokeLine(firstPoint.getKey(), firstPoint.getValue(), xNewEnd, firstPoint.getValue());
                nodes.set( 0, new MutablePair<>( (int) xNewEnd, firstPoint.getValue()) );
                context.strokeLine(xNewEnd, yNewEnd, xNewEnd, firstPoint.getValue());
                nodes.add( new MutablePair<>( (int) xNewEnd, (int) yNewEnd ) );
            } else
            {
                context.strokeLine(firstPoint.getKey(), firstPoint.getValue(), firstPoint.getKey(), yNewEnd);
                nodes.add( new MutablePair<>( firstPoint.getKey(), (int) yNewEnd ) );
                context.strokeLine(xNewEnd, yNewEnd, firstPoint.getKey(), yNewEnd);
            }
        }
        else
        {
            if( firstPoint.getValue() > yNewEnd )
            {
                context.strokeLine(firstPoint.getKey(), firstPoint.getValue(), firstPoint.getKey(), yNewEnd);
                nodes.add( new MutablePair<>( firstPoint.getKey(), (int) yNewEnd ) );
                context.strokeLine(xNewEnd, yNewEnd, firstPoint.getKey(), yNewEnd);
            }
            else
            {
                context.strokeLine(firstPoint.getKey(), firstPoint.getValue(), xNewEnd, firstPoint.getValue());
                nodes.set( 0, new MutablePair<>( (int) xNewEnd, firstPoint.getValue()) );
                context.strokeLine(xNewEnd, yNewEnd, xNewEnd, firstPoint.getValue());
                nodes.add( new MutablePair<>( (int) xNewEnd, (int) yNewEnd ) );
            }
        }
    }
}
