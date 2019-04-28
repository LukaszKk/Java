package sample;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Calculator
{
    private TextField t;
    private ArrayList<MutablePair<Integer, Integer>> nodes;
    private final double E;
    private double B;
    private double[][] points;
    private int pointsCnt;
    private double[][] potential;

    Calculator( Pane pane )
    {
        Label label = new Label("B: ");
        label.setLayoutX( 160 );
        label.setLayoutY( 13 );
        t = new TextField();
        t.setMaxSize( 50, 20 );
        t.setLayoutX( 175 );
        t.setLayoutY( 10 );

        pane.getChildren().addAll(label, t);
        E = 1E-8;
        pointsCnt = 0;
        B = 1;
    }

    void setNodes( ArrayList<MutablePair<Integer, Integer>> nodes )
    {
        this.nodes = nodes;
    }

    void calc()
    {
        ArrayList<MutablePair<Integer, Integer>> rNodes = getRectNodes(nodes);
        System.out.println( nodes );
        Polygon figure = new Polygon();
        for( MutablePair<Integer, Integer> node : nodes )
        {
            figure.getPoints().addAll((double) node.left, (double) node.right);
        }

        potential = new double[rNodes.get(2).left + 1][rNodes.get(2).right + 1];

        points = new double[rNodes.get(2).left + 1][rNodes.get(2).right + 1];
        for( int i = 0; i <= rNodes.get(2).left; i++ )
        {
            Arrays.fill(points[i], 0);
            Arrays.fill(potential[i], 0);
        }
        setEdge();

        for( int i = rNodes.get(0).left; i <= rNodes.get(2).left; i++ )
        {
            for( int j = rNodes.get(0).right; j <= rNodes.get(2).right; j++ )
            {
                if( !figure.contains(i, j) )
                    continue;

                pointsCnt++;
            }
        }

        t.setOnKeyPressed(keyEvent -> {
            if( keyEvent.getCode() == KeyCode.ENTER && Main.enter )
            {
                String text = t.getText();
                if( !text.equals("") )
                {
                    B = Integer.parseInt(text);
                    calcPotential(rNodes.get(0).left, rNodes.get(2).left, rNodes.get(0).right, rNodes.get(2).right);
                }
            }
        });

        /*for( int i = rNodes.get(0).left; i <= rNodes.get(2).left; i++ )
        {
            for( int j = rNodes.get(0).right; j <= rNodes.get(2).right; j++ )
                //if( !figure.contains(i, j) )
                //    System.out.print( " " );
                //else
                    System.out.print(points[i][j]);
            System.out.println();
        }*/
    }

    private void calcPotential( int left0, int leftN, int right0, int rightN )
    {
        Boolean[][] condition = new Boolean[leftN-left0-1][rightN-right0-1];
        for( int i = 0; i < leftN-left0-1; i++ )
            for( int j = 0; j < rightN-right0-1; j++)
                condition[i][j] = false;

        int cnt = 0;
        while( true )
        {
            for( int i = left0 + 1; i < leftN; i++ )
            {
                for( int j = right0 + 1; j < rightN; j++ )
                {
                    points[i][j] = potential[i][j];
                }
            }

            for( int i = left0 + 1; i < leftN; i++ )
            {
                for( int j = right0 + 1; j < rightN; j++ )
                {
                    potential[i][j] = (1 - B) * points[i][j] + B / 4 * (points[i + 1][j] + points[i - 1][j] + points[i][j + 1] + points[i][j - 1]);
                    if( Math.abs(potential[i][j] - points[i][j]) < E )
                        condition[i-left0-1][j-right0-1] = true;
                    //System.out.print( potential[i][j] );
                }
                //System.out.println();
            }

            cnt++;
            boolean is = true;
            for( int i = 0; i < leftN-left0-1; i++ )
            {
                for( int j = 0; j < rightN - right0 - 1; j++ )
                {
                    if( condition[i][j].equals(false) )
                    {
                        is = false;
                        break;
                    }
                }
                if( !is )
                    break;
            }

            if( is )
                break;

            System.out.println( cnt );
            /*for( int i = 0; i < leftN-left0-1; i++ )
            {
                for( int j = 0; j < rightN - right0 - 1; j++ )
                    System.out.print(condition[i][j] + ",");
                System.out.println();
            }
            System.out.println();
            */
        }

        t.setText( Integer.toString(cnt) );
    }

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
        rNodes.add( new MutablePair<>(minX, minY) );
        rNodes.add( new MutablePair<>(maxX, minY) );
        rNodes.add( new MutablePair<>(maxX, maxY) );
        rNodes.add( new MutablePair<>(minX, maxY) );

        return rNodes;
    }

    private boolean isEdge( int x, int y )
    {
        int i = 1;
        for( ; i < nodes.size(); i++ )
        {
            if( nodes.get(i).left.equals(nodes.get(i - 1).left) )
                if( nodes.get(i).left.equals(x) )
                    return true;
            else
                if( nodes.get(i).right.equals(y) )
                    return true;
        }
        if( nodes.get(i-1).left.equals(nodes.get(0).left) )
            if( nodes.get(0).left.equals(x) )
                return true;
        else
            if( nodes.get(0).right.equals(y) )
                return true;

        return false;
    }

    private void setEdge()
    {
        int i = 1;
        Random rand = new Random();
        for( ; i < nodes.size(); i++ )
        {
            int r = rand.nextInt(8)+1;
            if( nodes.get(i).left.equals(nodes.get(i - 1).left) )
            {
                if( nodes.get(i).right < nodes.get(i-1).right )
                {
                    for( int j = nodes.get(i).right; j <= nodes.get(i-1).right; j++ )
                    {
                        points[nodes.get(i).left][j] = r;
                    }
                }
                else
                {
                    for( int j = nodes.get(i-1).right; j <= nodes.get(i).right; j++ )
                    {
                        points[nodes.get(i).left][j] = r;
                    }
                }
            }
            else
            {
                if( nodes.get(i).left < nodes.get(i-1).left )
                {
                    for( int j = nodes.get(i).left; j <= nodes.get(i-1).left; j++ )
                    {
                        points[j][nodes.get(i).right] = r;
                    }
                }
                else
                {
                    for( int j = nodes.get(i-1).left; j <= nodes.get(i).left; j++ )
                    {
                        points[j][nodes.get(i).right] = r;
                    }
                }
            }
        }

        int r = rand.nextInt(8)+1;
        if( nodes.get(0).left.equals(nodes.get(i - 1).left) )
        {
            if( nodes.get(i-1).right < nodes.get(0).right )
            {
                for( int j = nodes.get(i-1).right; j <= nodes.get(0).right; j++ )
                {
                    points[nodes.get(0).left][j] = r;
                }
            }
            else
            {
                for( int j = nodes.get(0).right; j <= nodes.get(i-1).right; j++ )
                {
                    points[nodes.get(0).left][j] = r;
                }
            }
        }
        else
        {
            if( nodes.get(0).left < nodes.get(i-1).left )
            {
                for( int j = nodes.get(0).left; j <= nodes.get(i-1).left; j++ )
                {
                    points[j][nodes.get(0).right] = r;
                }
            }
            else
            {
                for( int j = nodes.get(i-1).left; j <= nodes.get(0).left; j++ )
                {
                    points[j][nodes.get(0).right] = r;
                }
            }
        }
    }
}
