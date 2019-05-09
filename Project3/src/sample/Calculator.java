package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Calculator
{
    private TextField t;
    private ArrayList<MutablePair<Integer, Integer>> nodes;
    private final double E;
    private double B;
    private double[][] points;
    //private int pointsCnt;
    private Double[][] potential;
    private Color[] colors;
    private Canvas canvas;
    private Polygon figure;
    private Label labelText;
    private Label labelText2;
    private Label labelVal1;
    private Label labelVal2;
    private ArrayList<MutablePair<Integer, Integer>> rNodes;

    //=========================================================================================================//
    Calculator( Pane pane, Canvas canvas )
    {
        this.canvas = canvas;
        labelText = new Label("B: ");
        labelText2 = new Label("");
        labelVal1 = new Label("");
        labelVal2 = new Label("");
        t = new TextField();
        t.setMaxSize(50, 20);
        labelText.setLayoutX(160);
        labelText.setLayoutY(13);
        t.setLayoutX(175);
        t.setLayoutY(10);
        t.setEditable(false);
        labelText2.setLayoutX(235);
        labelText2.setLayoutY(13);
        labelVal1.setLayoutX(5);
        labelVal1.setLayoutY(80);
        labelVal2.setLayoutX(5);
        labelVal2.setLayoutY(310);

        pane.getChildren().addAll(labelText, t, labelText2, labelVal1, labelVal2);
        E = 1E-8;
        //pointsCnt = 0;
        B = 1;

        colors = new Color[]{ new Color(244 / 255d, 66 / 255d, 66 / 255d, 1),
                new Color(244 / 255d, 98 / 255d, 65 / 255d, 1),
                new Color(244 / 255d, 181 / 255d, 65 / 255d, 1),
                new Color(199 / 255d, 244 / 255d, 65 / 255d, 1),
                new Color(106 / 255d, 244 / 255d, 65 / 255d, 1),
                new Color(65 / 255d, 244 / 255d, 193 / 255d, 1),
                new Color(65 / 255d, 160 / 255d, 244 / 255d, 1),
                new Color(65 / 255d, 73 / 255d, 244 / 255d, 1),
                new Color(163 / 255d, 65 / 255d, 244 / 255d, 1),
                new Color(244 / 255d, 65 / 255d, 217 / 255d, 1) };
    }

    //=========================================================================================================//
    void setNodes( ArrayList<MutablePair<Integer, Integer>> nodes )
    {
        this.nodes = nodes;
        rNodes = getRectNodes(nodes);
        //System.out.println(nodes);
        figure = new Polygon();
        figure.getPoints().addAll((double) nodes.get(0).left, (double) nodes.get(0).right);
        for( int i = 1; i < nodes.size(); i++ )
        {
            MutablePair<Integer, Integer> node = nodes.get(i);
            figure.getPoints().addAll((double) node.left, (double) node.right);
        }
        potential = new Double[rNodes.get(2).left + 1][rNodes.get(2).right + 1];
        points = new double[rNodes.get(2).left + 1][rNodes.get(2).right + 1];
    }

    //=========================================================================================================//
    void calc()
    {
        /*for( int i = rNodes.get(0).left; i <= rNodes.get(2).left; i++ )
        {
            for( int j = rNodes.get(0).right; j <= rNodes.get(2).right; j++ )
            {
                if( !isEdge(j, i) && !figure.contains(i, j) )
                    continue;

                pointsCnt++;
            }
        }*/

        t.setOnKeyPressed(keyEvent ->
        {
            if( keyEvent.getCode() == KeyCode.ENTER && Main.enter )
            {
                String text = t.getText();
                if( !text.equals("") )
                {
                    for( int i = 0; i <= rNodes.get(2).left; i++ )
                    {
                        Arrays.fill(potential[i], 0d);
                    }
                    for( int i = 0; i < rNodes.get(2).right + 1; i++ )
                    {
                        for( int j = 0; j < rNodes.get(2).left + 1; j++ )
                        {
                            if( isEdge(j, i) )
                                continue;
                            points[j][i] = 0;
                        }
                    }

                    B = Double.parseDouble(text);
                    try
                    {
                        calcPotential(rNodes.get(0).left, rNodes.get(2).left, rNodes.get(0).right, rNodes.get(2).right);
                        createMap(rNodes.get(0).left, rNodes.get(2).left, rNodes.get(0).right, rNodes.get(2).right);
                    }
                    catch( ArithmeticException e )
                    {
                        labelText2.setText(e.getMessage());
                    }
                }
            }
        });
    }

    //=========================================================================================================//
    private void calcPotential( int left0, int leftN, int right0, int rightN ) throws ArithmeticException
    {
        //labelText2.setText("Counting");
        Boolean[][] condition = new Boolean[leftN][rightN];
        for( int i = 0; i < leftN; i++ )
            Arrays.fill(condition[i], false);

        /*for( int i = right0; i <= rightN; i++ )
        {
            for( int j = left0; j <= leftN; j++ )
            {
                System.out.print( points[j][i] + " " );
            }
            System.out.println();
        }*/

        int cnt = 0;
        while( true )
        {
            for( int i = right0; i < rightN; i++ )
            {
                for( int j = left0; j < leftN; j++ )
                {
                    if( !isEdge(j, i) && !figure.contains(j, i) )
                        continue;
                    points[j][i] = potential[j][i];
                }
            }
            for( int i = right0; i < rightN; i++ )
            {
                for( int j = left0; j < leftN; j++ )
                {
                    if( !isEdge(j, i) && !figure.contains(j, i) )
                        continue;

                    potential[j][i] = ((1 - B) * points[j][i]) + (B / 4 * (points[j - 1][i] + points[j + 1][i] + points[j][i - 1] + points[j][i + 1]));
                    if( potential[j][i].isInfinite() || potential[j][i].isNaN() )
                        throw new ArithmeticException("To big value");
                    if( /*!condition[j][i] &&*/ (Math.abs(potential[j][i] - points[j][i]) < E) )
                        condition[j][i] = true;
                    //System.out.print(points[j][i] + "," + potential[j][i] + "," + (condition[j][i] ? "T" : "F") + " ");
                }
                //System.out.println();
            }
            //System.out.println();

            cnt++;
            boolean is = true;
            for( int i = right0; i < rightN; i++ )
            {
                for( int j = left0; j < leftN; j++ )
                {
                    if( condition[j][i] || (!isEdge(j, i) && !figure.contains(j, i)) )
                        continue;
                    is = false;
                    for( int k = left0; k < leftN; k++ )
                        Arrays.fill( condition[k], false );
                    break;
                }
                if( !is )
                    break;
            }
            if( is )
                break;
        }
        labelText2.setText("Count: " + cnt);
    }

    //=========================================================================================================//
    private void drawLegend( double min, double max )
    {
        canvas.getGraphicsContext2D().clearRect(0, 310, 20, 330);
        labelVal2.setText(Double.toString(min));
        canvas.getGraphicsContext2D().setFill(colors[9]);
        canvas.getGraphicsContext2D().clearRect(0, 95, 20, 115);
        labelVal1.setText(Double.toString(max));
        for( int i = 0, j = 9; i < 10; ++i, --j )
        {
            canvas.getGraphicsContext2D().setFill(colors[j]);
            canvas.getGraphicsContext2D().fillRect(0, 100 + i * 20, 20, 20);
        }
    }

    //=========================================================================================================//
    private void createMap( int left0, int leftN, int right0, int rightN )
    {
        double max = -Double.MAX_VALUE;
        double min = Double.MAX_VALUE;
        for( int i = right0; i < rightN; i++ )
        {
            for( int j = left0; j < leftN; j++ )
            {
                if( !isEdge(j, i) && !figure.contains(j, i) )
                    continue;

                max = Math.max(potential[j][i], max);
                min = Math.min(potential[j][i], min);
            }
        }

        drawLegend(min, max);

        for( int i = right0 + 1; i < rightN; i++ )
        {
            for( int j = left0 + 1; j < leftN; j++ )
            {
                if( !isEdge(j, i) && !figure.contains(j, i) )
                    continue;

                Color c = colors[getColor(potential[j][i], min, max)];
                canvas.getGraphicsContext2D().setFill(c);
                canvas.getGraphicsContext2D().fillRect(j, i, 1, 1);
            }
        }
    }

    //=========================================================================================================//
    private int getColor( double val, double min, double max )
    {
        double one = (max - min) / 10;
        for( int i = 0; i < 10; min += one, ++i )
        {
            if( val <= min )
                return i;
        }

        return 9;
    }

    //=========================================================================================================//
    private ArrayList<MutablePair<Integer, Integer>> getRectNodes( ArrayList<MutablePair<Integer, Integer>> nodes )
    {
        ArrayList<MutablePair<Integer, Integer>> rNodes = new ArrayList<>();

        int minX = nodes.get(0).left;
        int maxX = nodes.get(0).left;
        int minY = nodes.get(0).right;
        int maxY = nodes.get(0).right;
        for( MutablePair<Integer, Integer> node : nodes )
        {
            if( node.left < minX )
                minX = node.left;
            else if( node.left > maxX )
                maxX = node.left;

            if( node.right < minY )
                minY = node.right;
            else if( node.right > maxY )
                maxY = node.right;
        }
        rNodes.add(new MutablePair<>(minX, minY));
        rNodes.add(new MutablePair<>(maxX, minY));
        rNodes.add(new MutablePair<>(maxX, maxY));
        rNodes.add(new MutablePair<>(minX, maxY));

        return rNodes;
    }

    //=========================================================================================================//
    private boolean isEdge( int x, int y )
    {
        int i = 1;
        if( x == nodes.get(0).left && y == nodes.get(0).right )
            return true;
        for( ; i < nodes.size(); i++ )
        {
            if( x == nodes.get(i).left && y == nodes.get(i).right )
                return true;
            if( nodes.get(i).left.equals(nodes.get(i - 1).left) && nodes.get(i).left.equals(x) )
            {
                if( (nodes.get(i - 1).right < y && y < nodes.get(i).right) || (nodes.get(i).right < y && y < nodes.get(i - 1).right) )
                    return true;
            } else if( nodes.get(i).right.equals(nodes.get(i - 1).right) && nodes.get(i).right.equals(y) )
            {
                if( (nodes.get(i - 1).left < x && x < nodes.get(i).left) || (nodes.get(i).left < x && x < nodes.get(i - 1).left) )
                    return true;
            }
        }
        if( (nodes.get(i - 1).left.equals(nodes.get(0).left)) && nodes.get(i - 1).left.equals(x) )
        {
            return (nodes.get(i - 1).right < y && y < nodes.get(0).right) || (nodes.get(0).right < y && y < nodes.get(i - 1).right);
        } else if( nodes.get(0).right.equals(nodes.get(i - 1).right) && nodes.get(0).right.equals(y) )
        {
            return (nodes.get(i - 1).left < x && x < nodes.get(0).left) || (nodes.get(0).left < x && x < nodes.get(i - 1).left);
        }

        return false;
    }

    //=========================================================================================================//
    void setEdge()
    {
        /*int i = 1;
        Random rand = new Random();
        for( ; i < nodes.size(); i++ )
        {
            double r = rand.nextDouble();
            if( nodes.get(i).left.equals(nodes.get(i - 1).left) )
            {
                if( nodes.get(i).right < nodes.get(i - 1).right )
                {
                    for( int j = nodes.get(i).right; j <= nodes.get(i - 1).right; j++ )
                        points[nodes.get(i).left][j] = r;
                } else
                {
                    for( int j = nodes.get(i - 1).right; j <= nodes.get(i).right; j++ )
                        points[nodes.get(i).left][j] = r;
                }
            } else
            {
                if( nodes.get(i).left < nodes.get(i - 1).left )
                {
                    for( int j = nodes.get(i).left; j <= nodes.get(i - 1).left; j++ )
                        points[j][nodes.get(i).right] = r;
                } else
                {
                    for( int j = nodes.get(i - 1).left; j <= nodes.get(i).left; j++ )
                        points[j][nodes.get(i).right] = r;
                }
            }
        }

        double r = rand.nextDouble();
        if( nodes.get(0).left.equals(nodes.get(i - 1).left) )
        {
            if( nodes.get(i - 1).right < nodes.get(0).right )
            {
                for( int j = nodes.get(i - 1).right; j <= nodes.get(0).right; j++ )
                    points[nodes.get(0).left][j] = r;
            } else
            {
                for( int j = nodes.get(0).right; j <= nodes.get(i - 1).right; j++ )
                    points[nodes.get(0).left][j] = r;
            }
        } else
        {
            if( nodes.get(0).left < nodes.get(i - 1).left )
            {
                for( int j = nodes.get(0).left; j <= nodes.get(i - 1).left; j++ )
                    points[j][nodes.get(0).right] = r;
            } else
            {
                for( int j = nodes.get(i - 1).left; j <= nodes.get(0).left; j++ )
                    points[j][nodes.get(0).right] = r;
            }
        }
        t.setEditable(true);
         */


        List<MutablePair<MutablePair<Integer,Integer>, MutablePair<Integer,Integer>>> pairs = new ArrayList<>();
        int i;
        for( i = 1; i < nodes.size(); i++ )
        {
            pairs.add(new MutablePair<>(nodes.get(i-1), nodes.get(i)));
        }
        pairs.add(new MutablePair<>(nodes.get(i-1), nodes.get(0)));


        Stage stage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(new ScrollPane(root), 250, 300);
        stage.setScene(scene);

        AtomicInteger j = new AtomicInteger(0);
        for( int k = 0; k < pairs.size(); k++ )
        {
            var pair = pairs.get(k);
            HBox hBox = new HBox();
            TextField tf = new TextField();
            Label l = new Label("Edge " + (k+1) + " ");
            hBox.getChildren().addAll(l, tf);
            root.getChildren().add(hBox);
            tf.setOnAction(e ->
            {
                if( !tf.getText().equals("") )
                {
                    tf.setVisible(false);

                    if( pair.left.left.equals(pair.right.left) )
                    {
                        if( pair.right.right < pair.left.right )
                        {
                            for( int m = pair.right.right; m <= pair.left.right; m++ )
                                points[pair.right.left][m] = Double.parseDouble(tf.getText());
                        } else
                        {
                            for( int m = pair.left.right; m <= pair.right.right; m++ )
                                points[pair.right.left][m] = Double.parseDouble(tf.getText());
                        }
                    } else
                    {
                        if( pair.right.left < pair.left.left )
                        {
                            for( int m = pair.right.left; m <= pair.left.left; m++ )
                                points[m][pair.right.right] = Double.parseDouble(tf.getText());
                        } else
                        {
                            for( int m = pair.left.left; m <= pair.right.left; m++ )
                                points[m][pair.right.right] = Double.parseDouble(tf.getText());
                        }
                    }

                    j.incrementAndGet();
                    if( j.get() == nodes.size() )
                    {
                        t.setEditable(true);
                        stage.close();
                    }
                }
            });
        }
        stage.show();
    }
}
