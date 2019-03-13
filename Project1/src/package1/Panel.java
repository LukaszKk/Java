package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

public class Panel extends JPanel implements ClipboardOwner
{
    private final int padding = 25;
    private final float min = -1f;
    private final float max = 4.2f;
    private final Color lightGrayColor = new Color(200, 200, 200, 200);
    private final Color redColor = new Color(255, 0, 0);
    private final Color lightBlueColor = new Color(0, 0, 255, 125);
    private final int numberY = 5;
    private final int numberYY = 8;
    private final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private float[] arrayX;
    private float[] threeParams;
    private float[] arrayY;
    private int positionMult;

    Panel()
    {
        FileReader fileReader = new FileReader();
        arrayX = fileReader.read(0, 120);
        positionMult = 0;
    }

    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponents(graphics);
        Graphics2D newGraphics = (Graphics2D) graphics;
        newGraphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        newGraphics.setColor(Color.WHITE);
        newGraphics.fillRect(3*padding, 2*padding, getWidth() - 10 * padding, getHeight() - 4 * padding);
        //newGraphics.fillRect(getWidth()/10, 2*padding, 11*getWidth()/16, getHeight() - 4 * padding);
        newGraphics.setColor(Color.BLACK);

        //draw Y lines
        for (int i = 0; i <= numberY; i++)
        {
            int x = 3*padding;
            int y = getHeight() - ((i * (getHeight() - 4*padding))/numberY + 2*padding);

            newGraphics.setColor(lightBlueColor);
            newGraphics.drawLine(x-3, y, getWidth() - 7*padding, y);

            if( i > 0 )
            {
                int yRectHeight = (getHeight() - ((2 * (getHeight() - 4 * padding)) / numberY + 2 * padding)) - (getHeight() - ((3 * (getHeight() - 4 * padding)) / numberY + 2 * padding));
                int yy;
                int h = 0;
                for( int j = 0; j < numberYY; j++ )
                {
                    h += (j+1)*yRectHeight/48;
                    yy = y + h;

                    newGraphics.setColor(lightGrayColor);
                    newGraphics.drawLine(x, yy, getWidth() - 7 * padding, yy);
                }
            }

            newGraphics.setColor(Color.BLACK);
            String yLabel = "10^" + (int) (((int) ((min + (max - min) * i/numberY)*100))/100.0) + "";  //????
            FontMetrics metrics = newGraphics.getFontMetrics();
            int labelWidth = metrics.stringWidth(yLabel);
            newGraphics.drawString(yLabel, x - labelWidth - 5, y + (metrics.getHeight()/2) - 3);
        }

        newGraphics.setColor(redColor);
        String yLabel = "Ra/Rm";
        FontMetrics metrics = newGraphics.getFontMetrics();
        int labelWidth = metrics.stringWidth(yLabel);
        newGraphics.drawString(yLabel, 2*padding - labelWidth - 5, getHeight()/2 + (metrics.getHeight()/2) - 3);

        //draw X lines
        for( int i = 0; i < arrayX.length; i++ )
        {
            int x = i * (getWidth() - 10 * padding) / (arrayX.length - 1) + 3 * padding;
            int y = getHeight() - 2 * padding;

            if( (i % ((int) ((arrayX.length / 3.0)) + 1)) == 0 || i == arrayX.length-1 )
            {
                newGraphics.setColor(lightBlueColor);
                newGraphics.drawLine(x, getHeight() - 2*padding + 3, x, 2*padding);

                if( i > 0 )
                {
                    int xRectWidth = (((arrayX.length-1) * (getWidth() - 10 * padding) / (arrayX.length - 1) + 3 * padding) - (3 * padding))/3;
                    int xx;
                    int h = 0;
                    for( int j = 0; j < numberYY; j++ )
                    {
                        h += (j+1)*xRectWidth/48;
                        xx = x - h;

                        newGraphics.setColor(lightGrayColor);
                        newGraphics.drawLine(xx, getHeight() - 2*padding, xx, 2*padding);
                    }
                }

                newGraphics.setColor(Color.BLACK);
                String xLabel = "10^" + Math.round(((int) (arrayX[i]*100))/100.0 - 0.1) + "";
                metrics = newGraphics.getFontMetrics();
                labelWidth = metrics.stringWidth(xLabel);
                newGraphics.drawString(xLabel, x - labelWidth / 2, y + metrics.getHeight() + 3);
            }
        }

        newGraphics.setColor(redColor);
        String xLabel = "L/d";
        metrics = newGraphics.getFontMetrics();
        labelWidth = metrics.stringWidth(xLabel);
        newGraphics.drawString(xLabel, (float) getWidth()/2 - 1.6f*padding - (float) labelWidth / 2, getHeight()-padding + metrics.getHeight() + 3);

        //23104 pary
        //read file and draw figure
        FileReader fileReader = new FileReader();
        int k = 0;
        int xk;
        int n;
        int red = 0;
        int green = 0;
        int blue = 0;
        for( int j = positionMult*38; j < positionMult*38+38; j++, k++ )
        {
            if( (j+38) > 23104 )
                break;

            threeParams = fileReader.read((j+1)*120+j*3, 3);
            arrayY = fileReader.read((j+1)*123, 120);

            int heightPadding = getHeight() / 20;
            if( k % 2 == 1 )
            {
                xk = 1;
                n = heightPadding/4-10;
            }
            else
            {
                xk = 0;
                n = 0;
            }
            newGraphics.setStroke(GRAPH_STROKE);
            newGraphics.setColor(new Color(red, 255-green, 255-blue));
            red = red + 25 > 255 ? 255-red + 25 : red + 25;
            green = green + 45 > 255 ? 255-green + 45 : green + 45;
            blue = blue + 35 > 255 ? 255-blue + 35 : blue + 35;

            xLabel = String.format("%.2f", threeParams[2]) + "";
            newGraphics.drawLine(getWidth() - 5 * padding - 20 + xk*3*padding, getHeight() - heightPadding - 5 - k*(5*getHeight()/38/6) - n,
                                 getWidth() - 5 * padding - 8 + xk*3*padding,  getHeight() - heightPadding - 5 - k*(5*getHeight()/38/6) - n);
            newGraphics.drawString(xLabel, (float) getWidth() - 5 - 5 * padding + xk*3*padding,
                                           getHeight() - heightPadding - k*(5*getHeight()/38/6) - n);

            //double xScale = ((double) getWidth() - 13 * padding) / (arrayY.length - 1);
            double xScale = ((double) getWidth() - 10 * padding) / (arrayY.length - 1);
            double yScale = ((double) getHeight() - 3 * padding) / (max - min);
            int xRectWidth = (((arrayX.length-1) * (getWidth() - 10 * padding) / (arrayX.length - 1) + 3 * padding) - (3 * padding))/3;

            float multiple = 1;
            List<Point> graphPoints = new ArrayList<>();
            for( int i = 0; i < arrayY.length; i++ )
            {
                //int x = (int) (i * xScale + 5.5 * padding);
                int x = (int) (i * xScale + 3*padding) + (int) (xRectWidth/2*multiple);
                multiple -= (float) 1/105;
                int y = (int) ((max - arrayY[i]) * yScale + padding);
                graphPoints.add(new Point(x, y));
            }

            //draw figure
            for( int i = 0; i < graphPoints.size() - 1; i++ )
            {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                newGraphics.drawLine(x1, y1, x2, y2);
            }
        }

        newGraphics.setColor(redColor);
        xLabel = "D/d= " + String.format("%.2f", threeParams[0]) + "  Ri/Rm= " + String.format("%.2f", threeParams[1]);
        String xRtLabel = "Rt/Rm";
        newGraphics.drawString(xLabel, 3*padding, padding + metrics.getHeight() + 3);
        newGraphics.drawString(xRtLabel, getWidth()-4*padding, padding + metrics.getHeight() + 3);
    }

    int getPositionMult()
    {
        return positionMult;
    }

    void setPositionMult( int positionMult )
    {
        this.positionMult = positionMult;
    }

    @Override
    public void lostOwnership(Clipboard clip, Transferable trans )
    {
        System.out.println( "Lost Clipboard Ownership" );
    }
}
