package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

class FileReaderer
{
    private static double[] fourDoubles;
    private static File file;
    private double[] values;

    double[] readFour( File file )
    {
        FileReaderer.file = file;

        fourDoubles = new double[4];
        try
        {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));

            int i = 0;
            int cnt = 0;
            while( ++cnt < 11 )
            {
                if( cnt > 6 )
                {
                    String tmp = inputStream.readLine();
                    Scanner scanner = new Scanner(tmp.replace(":", ""));
                    scanner.useLocale(Locale.US);
                    while( scanner.hasNext() )
                    {
                        if( scanner.hasNextDouble() )
                            fourDoubles[i++] = scanner.nextDouble();
                        else
                            scanner.next();
                    }
                    scanner.close();
                } else
                    inputStream.readLine();
            }
            inputStream.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return fourDoubles;
    }

    String[] readProfileNames( File file )
    {
        ArrayList<String> profileNames = new ArrayList<>();
        try
        {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));
            String line;

            boolean flagFirst = true;
            while( (line=inputStream.readLine()) != null )
            {
                String[] tmp = line.split(" ");
                if( tmp[0].equals("~A") )
                {
                    for( int i = 1; i < tmp.length; i++ )
                    {
                        if( tmp[i].length() < 2 )
                            continue;

                        if( flagFirst )
                        {
                            flagFirst = false;
                            continue;
                        }

                        profileNames.add( tmp[i] );
                    }
                    break;
                }
            }

            inputStream.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return profileNames.toArray(String[]::new);
    }

    double[] readData( int number )
    {
        try
        {
            BufferedReader inputStream = new BufferedReader(new FileReader(FileReaderer.file));
            String line;
            Scanner scanner = null;

            double value = fourDoubles[3];
            while( (line = inputStream.readLine()) != null )
            {
                scanner = new Scanner( line );
                scanner.useLocale(Locale.US);

                if( scanner.hasNextDouble() )
                {
                    value = scanner.nextDouble();
                    if( value == fourDoubles[0] )
                    {
                        break;
                    }
                }

                scanner.close();
            }

            int size = (int) ((fourDoubles[1] - fourDoubles[0])/fourDoubles[2]);
            values = new double[size];

            //Save values
            int i;
            int valuesIndex = 0;
            /*if( number == 0 )
            {
                values[0] = value;
            }
            else
            {
                for( i = 1; i < number; i++ )
                {
                    if( scanner != null )
                        scanner.nextDouble();
                }
                values[0] = scanner != null ? scanner.nextDouble() : fourDoubles[3];
                if( scanner != null )
                {
                    scanner.close();
                }
            }*/

            for( i = 1; i < number+1; i++ )
            {
                if( scanner != null )
                    scanner.nextDouble();
            }
            values[0] = scanner != null ? scanner.nextDouble() : fourDoubles[3];
            if( scanner != null )
            {
                scanner.close();
            }

            int linesCount = 0;
            while( ++linesCount < size )
            {
                scanner = new Scanner( inputStream.readLine() );
                scanner.useLocale(Locale.US);

                for( i = 0; i < number+1; i++ )
                {
                    scanner.nextDouble();
                }
                values[++valuesIndex] = scanner.nextDouble();

                scanner.close();
            }

            inputStream.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return values;
    }
}
