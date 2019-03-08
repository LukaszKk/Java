package package1;

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame
{
    private JButton upButton;
    private JButton downButton;
    private JButton closeButton;

    private Main()
    {
        super( "Teoretyczne krzywe gradientowe dla h/d > 32" );

        Panel panel = new Panel();

        this.setMinimumSize( new Dimension(450, 350) );
        this.setPreferredSize( new Dimension(800, 600) );

        upButton = new JButton( "UP" );
        downButton = new JButton( "DOWN" );
        closeButton = new JButton( "CLOSE" );

        downButton.setEnabled( false );

        upButton.addActionListener(e ->
        {
            if( panel.getPositionMult() < 607 && upButton == e.getSource() )
            {
                int n = 1;
                if( panel.getPositionMult() == 0 )
                    n = 38;
                panel.setPositionMult(panel.getPositionMult()+n);
                if( !downButton.isEnabled() )
                    downButton.setEnabled( true );
                if( panel.getPositionMult() == 607 )
                    upButton.setEnabled( false );
            }
            this.repaint();
        });

        downButton.addActionListener(e ->
        {
            if( panel.getPositionMult() > 0 && downButton == e.getSource() )
            {
                int n = 1;
                if( panel.getPositionMult() == 38 )
                    n = 38;
                panel.setPositionMult(panel.getPositionMult()-n);
                if( !upButton.isEnabled() )
                    upButton.setEnabled( true );
                if( panel.getPositionMult() == 0 )
                    downButton.setEnabled( false );
            }
            this.repaint();
        });

        closeButton.addActionListener(e -> System.exit(0));

        upButton.setBounds( 10, 4, 100, getHeight()/15 );
        downButton.setBounds( 110, 4, 100, getHeight()/15 );
        closeButton.setBounds( 220, 4, 100, getHeight()/15 );

        this.add( upButton );
        this.add( downButton );
        this.add( closeButton );

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.getContentPane().add( panel );
        this.pack();
        this.setLocationRelativeTo( null );
    }

    public static void main( String[] args )
    {
        new Main().setVisible( true );
    }
}