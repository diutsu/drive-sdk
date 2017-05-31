package pt.ist.drive.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by diutsu on 23/11/16.
 */
public class RandomGeneratedStream extends InputStream {
    
    private final int[] magicBytes;
    private final Long length;
    private Long index = Long.valueOf(0);
    private Random random = new Random();
    public static final int[] JPG_MAGIC_BYTES = {  0xFF, 0xD8, 0xFF, 0xE0 };

    public RandomGeneratedStream(Long length){
        this.length=length;
        this.magicBytes=JPG_MAGIC_BYTES;
    }
    
    
    @Override
    public int read() throws IOException {
        if ( index >= length){
            return -1;
        }
        int randomByte;
        if(index < magicBytes.length){
            randomByte = magicBytes[index.intValue()];
        } else {
            randomByte = random.nextInt(255);
        }
        
        index++;
        return randomByte;
    }
}
