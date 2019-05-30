package app;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;


@SuppressWarnings( "serial" )
public class Kulki extends JFrame
{
    static int maxKulki = 5; //!!!???
    static final int planszaSzer = 9;
    static final int planszaWys = 9;
    static final int kulkaRoz = 50;

    static final int panelSzer = (planszaSzer * kulkaRoz) - 1;
    static final int panelWys = (planszaWys * kulkaRoz) - 1;

    static int punkty;

    static int gameO = 0;

    private static Color myellow = new Color(255, 255, 0);
    private static Color marine = new Color(0, 0, 255);
    private static Color green = new Color(0, 255, 0);
    private static Color grass = new Color(255, 0, 255);
    private static Color purple = new Color(125, 0, 125);
    private static Color coral = new Color(255, 0, 0);
    private static Color orange = new Color(255, 125, 0);
    private static Color red = new Color(125, 125, 125);
    private static Color blue = new Color(0, 255, 255);

    static final Color[] kPlanszy =
            {
                    new Color(255, 255, 255), //puste
                    new Color(255, 255, 255), //new Color(211, 239, 239), //dostepne
                    new Color(220, 223, 97), //zaznaczony
                    //new Color(189, 223, 191), //zaznaczony
                    //new Color(255, 247, 178)  //nowa kulka
                    new Color(255, 146, 152)  //nowa kulka
            };

    static Color[] kKulka =
            {
                    myellow, marine, orange, grass, purple, coral, red, blue, green
            };

    static PoleGry poleGry;

    private Kulki()
    {
        super("Balls");
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-370, Toolkit.getDefaultToolkit().getScreenSize().height/2-240);
        this.initComponents();
        poleGry = new PoleGry();
        poleGry.setBounds(280, 20, 450, 450);
        poleGry.setBackground(Color.WHITE);
        jPanel1.add(poleGry);
        this.add(jPanel1);
        gameover.setVisible(false);
        //arrow.setVisible(false);
        twojwynik.setVisible(false);
        wpiszimie.setVisible(false);
        linia.setVisible(false);
        imie.setVisible(false);
        zapisz.setVisible(false);
        zapisz.addActionListener(e ->
        {
            String name = "wyniki5.txt";
            if( maxKulki == 5 )
                name = "wyniki5.txt";
            if( maxKulki == 7 )
                name = "wyniki7.txt";
            if( maxKulki == 9 )
                name = "wyniki9.txt";

            wczytajIZapisz(name);
        });

        kolor5.setSelected(true);

        kolor5.addActionListener(e ->
        {
            kolor7.setSelected(false);
            kolor9.setSelected(false);
            kolor5.setSelected(true);
            maxKulki = 5;
            //if( gameO == 1 )
            //    wczytaj("wyniki5.txt");
            //else
            //{
                kKulka[0] = myellow;
                kKulka[1] = marine;
                kKulka[2] = orange;
                kKulka[3] = grass;
                kKulka[4] = purple;
                poleGry.nowaGra();
                poleGry.drukPlansza();
            //}
        });

        kolor7.addActionListener(e ->
        {
            kolor5.setSelected(false);
            kolor9.setSelected(false);
            kolor7.setSelected(true);
            maxKulki = 7;
            //if( gameO == 1 )
            //    wczytaj("wyniki7.txt");
            //else
            //{
                kKulka[0] = myellow;
                kKulka[1] = marine;
                kKulka[2] = orange;
                kKulka[3] = grass;
                kKulka[4] = purple;
                kKulka[5] = coral;
                kKulka[6] = red;
                poleGry.nowaGra();
                poleGry.drukPlansza();
            //}
        });

        kolor9.addActionListener(e ->
        {
            kolor7.setSelected(false);
            kolor5.setSelected(false);
            kolor9.setSelected(true);
            maxKulki = 9;
            //if( gameO == 1 )
            //    wczytaj("wyniki9.txt");
            //else
            //{
                kKulka[0] = myellow;
                kKulka[1] = marine;
                kKulka[2] = orange;
                kKulka[3] = grass;
                kKulka[4] = purple;
                kKulka[5] = coral;
                kKulka[6] = red;
                kKulka[7] = blue;
                kKulka[8] = green;
                poleGry.nowaGra();
                poleGry.drukPlansza();
            //}
        });

        nowagra.addActionListener(e -> stopgame());

        wynikimenu.addActionListener(e ->
        {
            /*if( punkty > 0 && gameO == 0 )
            {
                Object[] opcje = { "Tak", "Nie" };
                int opcja = JOptionPane.showOptionDialog(null, "Napewno chcesz przerwać istniejącą rozgrywkę?", "Rozpoczęcie od początku", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);

                if( opcja == 0 )
                {
                    String name = "wyniki5.txt";
                    if( maxKulki == 5 )
                        name = "wyniki5.txt";
                    if( maxKulki == 7 )
                        name = "wyniki7.txt";
                    if( maxKulki == 9 )
                        name = "wyniki9.txt";
                    gameO = 1;
                    wczytaj(name);
                }
            } else
            {
                String name = "wyniki5.txt";
                if( maxKulki == 5 )
                    name = "wyniki5.txt";
                if( maxKulki == 7 )
                    name = "wyniki7.txt";
                if( maxKulki == 9 )
                    name = "wyniki9.txt";
                gameO = 1;
                wczytaj(name);
            }*/


            String name = "wyniki5.txt";
            if( maxKulki == 5 )
                name = "wyniki5.txt";
            if( maxKulki == 7 )
                name = "wyniki7.txt";
            if( maxKulki == 9 )
                name = "wyniki9.txt";

            wczytaj(name);

            JFrame frame = new JFrame();
            frame.getContentPane().add( tabWyniki );
            frame.setSize( 400, 300 );
            frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-200, Toolkit.getDefaultToolkit().getScreenSize().height/2-150);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private void stopgame()
    {
        poleGry.nowaGra();
        poleGry.drukPlansza();
        punktyLabel.setText("0");
        Kulki.gameover.setVisible(false);
        //Kulki.arrow.setVisible(false);
        Kulki.twojwynik.setVisible(false);
        Kulki.wpiszimie.setVisible(false);
        //Kulki.linia.setVisible(false);
        Kulki.imie.setVisible(false);
        Kulki.zapisz.setVisible(false);
        Kulki.poleGry.setVisible(true);
    }

    private void wczytajIZapisz( String name )
    {
        //write to file
        try
        {
            FileWriter fw = new FileWriter(name, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println(imie.getText() + ';' + punktyLabel.getText());
            fw.close();
            pw.close();

        } catch( IOException ee )
        {
            System.out.println("ERROR!!");
        }

        wczytaj(name);
    }

    private void wczytaj( String name )
    {
        DefaultTableModel model = (DefaultTableModel) tabWyniki.getModel();
        model.setRowCount(0);

        //read from file
        try
        {
            FileReader fr = new FileReader(name);
            BufferedReader br = new BufferedReader(fr);

            String str;
            String[] data;
            while( (str = br.readLine()) != null )
            {
                data = str.split(";");
                int nowy = Integer.parseInt(data[1].trim());
                //if( counter != 10 )
                //{
                model.addRow(new Object[]{ data[0], nowy });
                //counter++;
                //}
            }

        sortKeys();
        }
        catch( IOException ee )
        {
            System.out.println("File not found!!");
        }

        //write to file
        try
        {
            FileWriter fw = new FileWriter(name);
            PrintWriter pw = new PrintWriter(fw);
            for( int row = 0; row < tabWyniki.getRowCount(); row++ )
            {
                pw.print(tabWyniki.getValueAt(row, 0));
                pw.print(';');
                pw.println(tabWyniki.getValueAt(row, 1));
            }
            fw.close();
            pw.close();

        } catch( IOException ee )
        {
            System.out.println("ERROR!!");
        }

        model.setRowCount(0);
        int counter = 1;

        //read from file
        try
        {
            FileReader fr = new FileReader(name);
            BufferedReader br = new BufferedReader(fr);

            String str;
            String[] data;
            while( (str = br.readLine()) != null )
            {
                data = str.split(";");
                int nowy = Integer.parseInt(data[1].trim());
                if( counter <= 10 )
                {
                    model.addRow(new Object[]{ data[0], nowy });
                    counter++;
                }
                else
                    break;
            }

            sortKeys();
        } catch( IOException ee )
        {
            System.out.println("File not found!!");
        }

        /*poleGry.drukPlansza();
        gameover.setVisible(false);
        wpiszimie.setVisible(false);
        linia.setVisible(false);
        imie.setVisible(false);
        zapisz.setVisible(false);*/
        //Kulki.tabWyniki1.setVisible(true);
        //Kulki.tabWyniki.setVisible(true);
        //Kulki.poziom.setEnabled(true);
        //poleGry.setVisible(true);
        //Kulki.poleGry.setVisible(false);

        gameover.setText("Saved");
    }

    private void sortKeys()
    {
        RowSorter<TableModel> sorter = new TableRowSorter<>(tabWyniki.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>(10);
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        tabWyniki.setRowSorter(sorter);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jMenuItem1 = new JMenuItem();
        jMenu2 = new JMenu();
        jMenuItem2 = new JMenuItem();
        jMenuItem3 = new JMenuItem();
        jPanel1 = new JPanel();
        jLabel3 = new JLabel();
        punktyLabel = new JLabel();
        linia = new JSeparator();
        gameover = new JLabel();
        arrow = new JLabel();
        twojwynik = new JLabel();
        imie = new JTextField();
        wpiszimie = new JLabel();
        jSeparator2 = new JSeparator();
        zapisz = new Button();
        //tabWyniki1 = new JScrollPane();
        tabWyniki = new JTable();
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        nowagra = new JMenuItem();
        wynikimenu = new JMenuItem();
        poziom = new JMenu();
        kolor5 = new JRadioButtonMenuItem();
        kolor7 = new JRadioButtonMenuItem();
        kolor9 = new JRadioButtonMenuItem();

        jMenuItem1.setText("jMenuItem1");
        jMenu2.setText("jMenu2");
        jMenuItem2.setText("jMenuItem2");
        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //jPanel1.setBackground(new java.awt.Color(16, 42, 57));
        jPanel1.setBackground(new Color(85, 91, 90));
        //jPanel1.setPreferredSize(new Dimension(820, 550));
        jPanel1.setPreferredSize(new Dimension(740, 480));
        jPanel1.setLayout(null);

        //jLabel3.setFont(new Font("Century Gothic", Font.PLAIN, 30)); // NOI18N
        jLabel3.setFont(new Font("", Font.BOLD, 20)); // NOI18N
        jLabel3.setForeground(new Color(255, 255, 255));
        jLabel3.setText("Result:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 80, 102, 38);

        //punktyLabel.setFont(new Font("Century Gothic", Font.PLAIN, 30)); // NOI18N
        punktyLabel.setFont(new Font("", Font.PLAIN, 30)); // NOI18N
        punktyLabel.setForeground(new Color(255, 255, 255));
        punktyLabel.setText("0");
        jPanel1.add(punktyLabel);
        punktyLabel.setBounds(140, 80, 90, 38);
        jPanel1.add(linia);
        linia.setBounds(260, 400, 280, 10);

        //gameover.setFont(new Font("Century Gothic", Font.BOLD, 56)); // NOI18N
        gameover.setFont(new Font("", Font.BOLD, 30)); // NOI18N
        gameover.setForeground(new Color(255, 255, 51));
        gameover.setText("GAME OVER");
        jPanel1.add(gameover);
        gameover.setBounds(260, 50, 350, 90);

        //arrow.setIcon(new ImageIcon(getClass().getResource("/left-arrow (2).png"))); // NOI18N
        //jPanel1.add(arrow);
        //arrow.setBounds(260, 70, 50, 50);

        /*twojwynik.setFont(new Font("Century Gothic", Font.BOLD, 18)); // NOI18N
        twojwynik.setForeground(new Color(255, 255, 255));
        twojwynik.setText("Twój wynik");
        jPanel1.add(twojwynik);
        twojwynik.setBounds(320, 80, 120, 30);*/

        imie.setBackground(new Color(51, 96, 117));
        //imie.setFont(new Font("Century Gothic", Font.PLAIN, 18)); // NOI18N
        imie.setFont(new Font("", Font.PLAIN, 14)); // NOI18N
        imie.setForeground(new Color(255, 255, 255));
        imie.setBorder(null);
        jPanel1.add(imie);
        imie.setBounds(380, 150, 160, 30);

        //wpiszimie.setFont(new Font("Century Gothic", Font.PLAIN, 18)); // NOI18N
        wpiszimie.setFont(new Font("", Font.PLAIN, 14)); // NOI18N
        wpiszimie.setForeground(new Color(255, 255, 255));
        wpiszimie.setText("Write name:");
        jPanel1.add(wpiszimie);
        wpiszimie.setBounds(260, 150, 100, 30);
        //jPanel1.add(jSeparator2);
        jSeparator2.setBounds(20, 120, 220, 10);

        zapisz.setBackground(new Color(255, 255, 255));
        //zapisz.setFont(new Font("Century Gothic", Font.PLAIN, 18)); // NOI18N
        zapisz.setFont(new Font("", Font.PLAIN, 14)); // NOI18N
        zapisz.setLabel("Save");
        jPanel1.add(zapisz);
        zapisz.setBounds(350, 414, 120, 40);

        tabWyniki.setBackground(new Color(85, 91, 90));
        //tabWyniki.setBackground(new Color(59, 62, 66));
        //tabWyniki.setFont(new Font("Century Gothic", Font.PLAIN, 14)); // NOI18N
        tabWyniki.setFont(new Font("", Font.PLAIN, 12)); // NOI18N
        tabWyniki.setForeground(new Color(255, 255, 255));
        tabWyniki.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Imię", "Punkty"
                }
        )
        {
            Class[] types = new Class[]{
                    String.class, Integer.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public Class getColumnClass( int columnIndex )
            {
                return types[columnIndex];
            }

            public boolean isCellEditable( int rowIndex, int columnIndex )
            {
                return canEdit[columnIndex];
            }
        });

        tabWyniki.getTableHeader().setReorderingAllowed(false);
        //tabWyniki1.setOpaque(false);
        //tabWyniki1.getViewport().setOpaque(false);
        //tabWyniki.getTableHeader().setFont(new Font("Century Gothic", Font.PLAIN, 15));
        tabWyniki.getTableHeader().setFont(new Font("", Font.PLAIN, 12));
        //tabWyniki1.setViewportView(tabWyniki);

        //jPanel1.add(tabWyniki1);
        //tabWyniki1.setBounds(230, 140, 380, 370);

        jMenu1.setText("Game");

        nowagra.setText("New game");
        jMenu1.add(nowagra);

        wynikimenu.setText("Results");
        jMenu1.add(wynikimenu);

        jMenuBar1.add(jMenu1);

        poziom.setText("Level");

        kolor5.setSelected(true);
        kolor5.setText("5 colors");
        poziom.add(kolor5);

        kolor7.setText("7 colors");
        poziom.add(kolor7);

        kolor9.setText("9 colors");
        poziom.add(kolor9);

        jMenuBar1.add(poziom);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main( String[] args )
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new Kulki().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    static JLabel arrow;
    static JLabel gameover;
    static JTextField imie;
    private static JLabel jLabel3;
    private JMenu jMenu1;
    private JMenu jMenu2;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JPanel jPanel1;
    private JSeparator jSeparator2;
    private JRadioButtonMenuItem kolor5;
    private JRadioButtonMenuItem kolor7;
    private JRadioButtonMenuItem kolor9;
    static JSeparator linia;
    private JMenuItem nowagra;
    static JMenu poziom;
    static JLabel punktyLabel;
    static JTable tabWyniki;
    //static JScrollPane tabWyniki1;
    static JLabel twojwynik;
    static JLabel wpiszimie;
    private JMenuItem wynikimenu;
    static Button zapisz;
    // End of variables declaration//GEN-END:variables
}