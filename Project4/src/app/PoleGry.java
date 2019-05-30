package app;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;

@SuppressWarnings( "serial" )
public class PoleGry extends Canvas implements MouseListener
{

    private BufferedImage image = new BufferedImage(Kulki.panelSzer, Kulki.panelWys, BufferedImage.TYPE_INT_RGB);
    private Graphics2D grafika = (Graphics2D) image.getGraphics();

    private int[][] tabPlansza, tabKulki, move;

    private byte selectedX, selectedY;
    private int tempX, tempY, x1, y1, x2, y2, x3, y3;
    private boolean selected;
    private Random los = new Random();

    PoleGry()
    {
        super();
        this.addMouseListener(this);
        tabPlansza = new int[Kulki.planszaSzer][Kulki.planszaWys];
        tabKulki = new int[Kulki.planszaSzer][Kulki.planszaWys];
        move = new int[Kulki.planszaSzer][Kulki.planszaWys];
        this.nowaGra();
    }

    void nowaGra()
    {
        //Kulki.tabWyniki1.setVisible(false);
        //Kulki.tabWyniki1.setVisible(false);
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
            {
                tabKulki[x][y] = 0;
                tabPlansza[x][y] = 0;
            }

        nowaKulka();
        nowaKulka();
        nowaKulka();
        nowaKulka();
        nowaKulka();

        Kulki.punkty = 0;
        Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
        //.Kulki.punktyLabel.setText("0");
        selected = false;
        Kulki.gameO = 0;

        Kulki.gameover.setText("Game over");
    }


    @Override
    public void paint( Graphics g )
    {
        super.paint(g);
        this.drukPlansza();
    }

    void drukPlansza()
    {
        //if( Kulki.punkty > 0 && Kulki.gameO == 0 )
        //    Kulki.poziom.setEnabled(false);
        //else
        //  Kulki.poziom.setEnabled(true);
        this.plansza();
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    private void plansza()
    {
        int index = 0;
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
            {
                if( tabKulki[x][y] == 0 ) index++;

                grafika.setColor(Kulki.kPlanszy[tabPlansza[x][y]]);
                grafika.fillRect(x * Kulki.kulkaRoz, y * Kulki.kulkaRoz, Kulki.kulkaRoz, Kulki.kulkaRoz);

                if( move[x][y] == -2 )
                {
                    grafika.setColor(new Color(41, 118, 138, 80));
                    grafika.fillRect(x * Kulki.kulkaRoz, y * Kulki.kulkaRoz, Kulki.kulkaRoz, Kulki.kulkaRoz);
                }
                move[x][y] = 0;

                grafika.setColor(new Color(0, 0, 0));
                grafika.drawRect(x * Kulki.kulkaRoz, y * Kulki.kulkaRoz, Kulki.kulkaRoz, Kulki.kulkaRoz);

                if( tabKulki[x][y] > 0 )
                    this.kulka(x, y, tabKulki[x][y]);
            }

        if( index < 3 )
        {
            gameOver();
            Kulki.gameO = 1;
        }

    }

    private void gameOver()
    {
        Kulki.poleGry.setVisible(false);
        Kulki.gameover.setVisible(true);
        //Kulki.arrow.setVisible(true);
        Kulki.twojwynik.setVisible(true);
        Kulki.wpiszimie.setVisible(true);
        //Kulki.linia.setVisible(true);
        Kulki.imie.setVisible(true);
        Kulki.zapisz.setVisible(true);
        //Kulki.tabWyniki.setVisible(false);
        Kulki.poziom.setEnabled(true);
    }

    private void kulka( int x, int y, int k )
    {
        grafika.setColor(Kulki.kKulka[k - 1]);
        grafika.fillOval((x * Kulki.kulkaRoz) + 5, (y * Kulki.kulkaRoz) + 5, Kulki.kulkaRoz - 10, Kulki.kulkaRoz - 10);
    }

    @Override
    public void mouseClicked( MouseEvent e )
    {

    }

    @SuppressWarnings( "static-access" )
    @Override
    public void mousePressed( MouseEvent e )
    {
        int x = e.getX() / Kulki.kulkaRoz;
        int y = e.getY() / Kulki.kulkaRoz;

        if( e.getButton() == e.BUTTON1 )
        {
            if( tabKulki[x][y] > 0 && !selected )
            {
                selected = true;
                selectedX = (byte) x;
                selectedY = (byte) y;
                tabPlansza[x][y] = 2;


                drukPlansza();
            }
            else if( tabPlansza[x][y] == 1 )
            {
                tabKulki[x][y] = tabKulki[selectedX][selectedY];
                tabKulki[selectedX][selectedY] = 0;

                int[][] tmp = new int[9][9];
                for( int i = 0; i < 9; i++ )
                {
                    System.arraycopy(tabKulki[i], 0, tmp[i], 0, 9);
                }
                tmp[x][y] = 0;
                BFS( tmp, new Point(selectedX, selectedY), new Point(x, y) );

                czyscZaznaczone();
                if( !sprawdzamPionIPoziom() )
                {
                    nowaKulka();
                    x1 = tempX;
                    y1 = tempY;
                    nowaKulka();
                    x2 = tempX;
                    y2 = tempY;
                    nowaKulka();
                    x3 = tempX;
                    y3 = tempY;
                    //zaznaczNowe();
                } else Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));

                //if( sprawdzamSkos() )
                //    Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                if( sprawdzamKwadrat() )
                    Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                zaznaczNowe();
                if( sprawdzamPionIPoziom() )
                    Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                //if( sprawdzamSkos() )
                //    Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                if( sprawdzamKwadrat() )
                    Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));

                drukPlansza();
                czyscZaznaczone();
            } else
            {
                czyscZaznaczone();
                drukPlansza();
            }
        }
    }

    private void zaznaczNowe()
    {
        tabPlansza[x1][y1] = 3;
        tabPlansza[x2][y2] = 3;
        tabPlansza[x3][y3] = 3;
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        if( selected )
        {
            if( !dostepnePola() )
                czyscZaznaczone();

            drukPlansza();
        }
    }

    @Override
    public void mouseEntered( MouseEvent e )
    {

    }

    @Override
    public void mouseExited( MouseEvent e )
    {

    }

    private void czyscZaznaczone()
    {
        selected = false;
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
                tabPlansza[x][y] = 0;
    }

    private boolean zaznaczPole( int x, int y )
    {
        return tabKulki[x][y] == 0 && tabPlansza[x][y] == 0;
    }

    private boolean dostepnePola()
    {
        boolean pola = false;
        int index;

        do
        {
            index = 0;
            for( int x = 0; x < Kulki.planszaSzer; x++ )
                for( int y = 0; y < Kulki.planszaWys; y++ )
                    if( tabPlansza[x][y] > 0 )
                    {
                        if( x - 1 >= 0 && zaznaczPole(x - 1, y) )
                        {
                            index++;
                            tabPlansza[x - 1][y] = 1;
                            pola = true;
                        }
                        if( x + 1 < Kulki.planszaSzer && zaznaczPole(x + 1, y) )
                        {
                            index++;
                            tabPlansza[x + 1][y] = 1;
                            pola = true;
                        }
                        if( y - 1 >= 0 && zaznaczPole(x, y - 1) )
                        {
                            index++;
                            tabPlansza[x][y - 1] = 1;
                            pola = true;
                        }
                        if( y + 1 < Kulki.planszaWys && zaznaczPole(x, y + 1) )
                        {
                            index++;
                            tabPlansza[x][y + 1] = 1;
                            pola = true;
                        }
                    }
        } while( index > 0 );

        return pola;
    }

    private void nowaKulka()
    {
        int x, y;
        do
        {
            x = los.nextInt(Kulki.planszaSzer);
            y = los.nextInt(Kulki.planszaWys);

        } while( tabKulki[x][y] > 0 );
        tabKulki[x][y] = los.nextInt(Kulki.maxKulki) + 1;
        tempX = x;
        tempY = y;
    }

    class PointVal extends Point
    {
        PointVal( int x, int y )
        {
            super( x, y );
        }

        @Override
        public boolean equals(Object o)
        {
            if( !(o instanceof PointVal) )
                return false;
            return (this.x == ((PointVal) o).x) && (this.y == ((PointVal) o).y);
        }
    }

    private ArrayList<int[]> list = new ArrayList<>();
    private Map<PointVal, PointVal> temp = new HashMap<>();

    class QueueNode
    {
        Point pt;
        int dist;
        QueueNode( Point pt, int dist )
        {
            this.pt = pt;
            this.dist = dist;
        }
    }

    private boolean isValid( int row, int col )
    {
        return (row >= 0) && (row < 9) && (col >= 0) && (col < 9);
    }

    private int[] rowNum = { -1, 0, 0, 1 };
    private int[] colNum = { 0, -1, 1, 0 };

    private void BFS( int[][] mat, Point src, Point dest )
    {
        boolean[][] visited = new boolean[9][9];
        for( int i = 0; i < 9; i++ )
            for( int j = 0; j < 9; j++ )
                visited[i][j] = false;

        visited[src.x][src.y] = true;

        Queue<QueueNode> q = new LinkedList<>();
        QueueNode s = new QueueNode(src, 0);
        q.add(s);

        while( !q.isEmpty() )
        {
            QueueNode curr = q.peek();
            Point pt = curr.pt;

            if (pt.x == dest.x && pt.y == dest.y)
            {
                list.clear();

                PointVal p = temp.get(new PointVal(pt.x, pt.y));
                while( !(p.x == src.x && p.y == src.y) )
                {
                    list.add( new int[]{p.x, p.y} );
                    p = temp.get(new PointVal(p.x, p.y));
                }

                move[src.x][src.y] = -2;
                for( int[] ints : list )
                    move[ints[0]][ints[1]] = -2;
                move[dest.x][dest.y] = -2;

                return;
            }

            q.poll();

            for (int i = 0; i < 4; i++)
            {
                int row = pt.x + rowNum[i];
                int col = pt.y + colNum[i];

                if( isValid(row, col) && mat[row][col] == 0 && !visited[row][col])
                {
                    temp.put(new PointVal(row, col), new PointVal(pt.x, pt.y));

                    visited[row][col] = true;
                    QueueNode Adjcell = new QueueNode( new Point(row, col), curr.dist + 1 );
                    q.add(Adjcell);
                }
            }
        }
    }

    private boolean sprawdzamPionIPoziom()
    {
        boolean stan = false;
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
            {
                if( tabKulki[x][y] > 0 )
                {
                    if( linia1(x, y) )
                        stan = true;

                    if( linia2(x, y) )
                        stan = true;
                }
            }
        return stan;
    }

    private boolean sprawdzamSkos()
    {
        boolean stan = false;
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
            {
                if( tabKulki[x][y] > 0 )
                {
                    if( linia3(x, y) )
                        stan = true;

                    if( linia4(x, y) )
                        stan = true;
                }
            }
        return stan;
    }

    private boolean sprawdzamKwadrat()
    {
        boolean stan = false;
        for( int x = 0; x < Kulki.planszaSzer; x++ )
            for( int y = 0; y < Kulki.planszaWys; y++ )
            {
                if( tabKulki[x][y] > 0 )
                {
                    if( linia5(x, y) )
                        stan = true;
                }
            }
        return stan;
    }

    private boolean linia1( int x, int y ) // poziomo
    {
        if( tabKulki[x][y] == 0 ) return false;
        int index = 0;
        int kolor = tabKulki[x][y];
        for( int i = x; i < Kulki.planszaSzer; i++ )
        {
            if( tabKulki[i][y] == kolor )
            {
                index++;
            } else
            {
                if( index < 5 ) return false;
                else
                {
                    for( int j = i - 1; j > (x - 1); j-- )
                        tabKulki[j][y] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
            if( i == Kulki.planszaSzer - 1 )
            {
                if( index < 5 ) return false;
                else
                {
                    for( int j = i; j > (x - 1); j-- )
                        tabKulki[j][y] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean linia2( int x, int y ) //pionowo
    {
        if( tabKulki[x][y] == 0 ) return false;
        int index = 0;
        int kolor = tabKulki[x][y];
        for( int i = y; i < Kulki.planszaWys; i++ )
        {
            if( tabKulki[x][i] == kolor ) index++;
            else
            {
                if( index < 5 ) return false;
                else
                {
                    for( int j = i - 1; j > (y - 1); j-- )
                        tabKulki[x][j] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
            if( i == Kulki.planszaWys - 1 )
            {
                if( index < 5 ) return false;
                else
                {
                    for( int j = i; j > (y - 1); j-- )
                        tabKulki[x][j] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean linia3( int x, int y ) //skos z lewa na prawą
    {
        if( tabKulki[x][y] == 0 ) return false;
        int index = 0;
        int kolor = tabKulki[x][y];
        for( int i = x, j = y; i < Kulki.planszaSzer && j < Kulki.planszaWys; i++, j++ )
        {
            if( (tabKulki[i][j] == kolor) )
            {
                index++;
            } else
            {
                if( index < 5 ) return false;
                else
                {
                    for( int k = i - 1, l = j - 1; k > (x - 1) || l > (y - 1); k--, l-- )
                        tabKulki[k][l] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
            if( i == Kulki.planszaSzer - 1 || j == Kulki.planszaWys - 1 )
            {
                if( index < 5 ) return false;
                else
                {
                    for( int k = i, l = j; k > (x - 1) && l > (y - 1); k--, l-- )
                        tabKulki[k][l] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean linia4( int x, int y ) // skos z prawa na lewą
    {
        if( tabKulki[x][y] == 0 ) return false;
        int index = 0;
        int kolor = tabKulki[x][y];
        for( int i = x, j = y; i >= 0 && i < Kulki.planszaSzer && j < Kulki.planszaWys && j >= 0; i++, j-- )
        {
            if( (tabKulki[i][j] == kolor) )
            {
                index++;
            } else
            {
                if( index < 5 ) return false;
                else
                {
                    for( int k = i - 1, l = j + 1; (k > (x) && k >= 0) || l < Kulki.planszaWys; k--, l++ )
                    {
                        tabKulki[k][l] = 0;
                    }
                    dodajPunkty(index);
                    return true;
                }
            }
            if( i == Kulki.planszaSzer - 1 || j == 0 )
            {
                if( index < 5 ) return false;
                else
                {
                    for( int k = i, l = j; (k > (x - 1) && k >= 0) && l < Kulki.planszaWys; k--, l++ )
                        tabKulki[k][l] = 0;
                    dodajPunkty(index);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean linia5( int x, int y ) // kwadrat
    {
        if( tabKulki[x][y] == 0 ) return false;
        int index = 0;
        int kolor = tabKulki[x][y];
        for( int i = x, j = y; i < Kulki.planszaSzer && j < Kulki.planszaWys; i++, j++ )
        {
            if( tabKulki[i][y] == kolor && tabKulki[x][j] == kolor && tabKulki[i][j] == kolor )
            {
                index++;
            } else
            {
                if( index < 2 ) return false;
                else
                {
                    for( int k = i - 1; k > (x - 1); k-- )
                    {
                        tabKulki[k][y] = 0;
                        tabKulki[k][y + 1] = 0;
                    }
                    dodajPunkty(index);
                    return true;
                }
            }
            if( i == Kulki.planszaSzer - 1 || j == Kulki.planszaWys - 1 )
            {
                if( index < 2 ) return false;
                else
                {
                    for( int k = i; k > (x - 1); k-- )
                    {
                        tabKulki[k][y] = 0;
                        tabKulki[k][y + 1] = 0;
                    }
                    dodajPunkty(index);
                    return true;
                }
            }
        }
        return false;
    }


    private void dodajPunkty( int p )
    {
        switch( p )
        {
            case 2:
            case 8:
                Kulki.punkty += 4;
                Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                break;
            case 5:
            case 6:
            case 7:
            case 9:
                Kulki.punkty += 5;
                Kulki.punktyLabel.setText(String.valueOf(Kulki.punkty));
                break;
        }
    }

}
