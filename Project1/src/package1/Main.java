package package1;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Main extends JFrame
{
    private JMenuItem upButton;
    private JMenuItem downButton;
    private JMenuItem closeButton;
    private JPopupMenu jPopupMenu;
    private JMenuItem copyButton;
    private Panel panel;
    private JMenuBar jMenuBar;

    private Main()
    {
        super( "Teoretyczne krzywe gradientowe dla h/d > 32" );

        panel = new Panel();

        this.setMinimumSize( new Dimension(450, 350) );
        this.setPreferredSize( new Dimension(800, 600) );

        jMenuBar = new JMenuBar();

        upButton = new JMenuItem( "UP" );
        downButton = new JMenuItem( "DOWN" );
        closeButton = new JMenuItem( "CLOSE" );

        downButton.setEnabled( false );

        jPopupMenu = new JPopupMenu();
        copyButton = new JMenuItem( "Copy" );

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

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if( e.getButton() == MouseEvent.BUTTON3 )
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        copyButton.addActionListener(e ->
        {
            jPopupMenu.setVisible(false);
            Thread copyThread = new Thread(() ->
            {
                try
                {
                    Robot robot = new Robot();
                    Rectangle area = new Rectangle(panel.getLocationOnScreen().x, panel.getLocationOnScreen().y, panel.getWidth(), panel.getHeight());
                    BufferedImage i = robot.createScreenCapture(area);
                    TransferableImage trans = new TransferableImage(i);
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    c.setContents(trans, panel);
                }
                catch( AWTException x )
                {
                    x.printStackTrace();
                    System.exit(1);
                }
            });
            copyThread.start();
        });

        jMenuBar.add( upButton );
        jMenuBar.add( downButton );
        jMenuBar.add( closeButton );
        jPopupMenu.add(copyButton);
        this.setJMenuBar(jMenuBar);

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