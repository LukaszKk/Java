package package1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FileReader
{
    private final String fileName = "BAZA2.bin";

    float[] read( int position, int size )
    {
        float[] result = new float[size];

        try
        {
            FileChannel fileChannel;
            ByteBuffer byteBuffer;
            fileChannel = (FileChannel) Files.newByteChannel(Paths.get(fileName), StandardOpenOption.READ);

            if( fileChannel.size() <= position*4 )
                return null;

            fileChannel.position( position*4 );
            for( int i = 0; i < size; i++ )
            {
                byteBuffer = ByteBuffer.allocate(4);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                fileChannel.read(byteBuffer);
                byteBuffer.flip();
                result[i] = byteBuffer.getFloat();
            }
            fileChannel.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return result;
    }
}
