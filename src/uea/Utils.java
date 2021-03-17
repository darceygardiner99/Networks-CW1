package uea;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils
{
    public static void main(String[] args)
    {
        byte[] array = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

        byte[] encrypted = encrypt(array, 23429654);

        System.out.println(Arrays.toString(encrypted));

        byte[] decrypted = decrypt(encrypted, 23429654);

        System.out.println(Arrays.toString(decrypted));
    }

    public static byte[] encrypt(byte[] block, int key)
    {
        byte[] encrypted = new byte[block.length];

        ByteBuffer plainTex = ByteBuffer.wrap(block);

        for (int i = 0; i < block.length; i+=4)
        {
            int fourByte = plainTex.getInt();
            fourByte = fourByte ^ key;

            ByteBuffer holder = ByteBuffer.allocate(4).putInt(fourByte);

            for (int x = 0; x < 4; x++)
            {
                encrypted[i + x] = holder.get(x);
            }
        }

        ByteBuffer reversed = ByteBuffer.allocate(block.length);

        for (int i = encrypted.length - 1; i > -1; i--)
        {
            reversed.put(encrypted[i]);
        }

        return reversed.array();
    }

    public static byte[] decrypt(byte[] block, int key)
    {
        byte[] encrypted = ByteBuffer.wrap(block).array();

        ByteBuffer reversed = ByteBuffer.allocate(block.length);

        for (int i = encrypted.length - 1; i > -1; i--)
        {
            reversed.put(encrypted[i]);
        }

        byte[] decrypted = new byte[block.length];

        reversed.rewind();

        for (int i = 0; i < block.length; i+=4)
        {
            int fourByte = reversed.getInt();
            fourByte = fourByte ^ key;

            ByteBuffer holder = ByteBuffer.allocate(4).putInt(fourByte);

            for (int x = 0; x < 4; x++)
            {
                decrypted[i + x] = holder.get(x);
            }
        }

        return decrypted;
    }

    public static byte[] applyId(byte[] block, int id)
    {
        ByteBuffer newBlock = ByteBuffer.allocate(block.length + 4); // Id is 4 bytes, so add 4;

        newBlock.putInt(id);
        newBlock.put(block);

        return newBlock.array();
    }

    public static int readId(byte[] block)
    {
        return ByteBuffer.wrap(block).getInt();

    }

    public static byte[] removeId(byte[] block)
    {
        ByteBuffer newBlock = ByteBuffer.allocate(block.length - 4);
        newBlock.put(block, 4, block.length - 4);

        return newBlock.array();
    }

    public static byte[] applyTimestamp(byte[] block, long time)
    {
        ByteBuffer newBlock = ByteBuffer.allocate(block.length + 8); // Long is 8 bytes, so add 8

        newBlock.putLong(time);
        newBlock.put(block);

        return newBlock.array();
    }

    public static long readTimestamp(byte[] block)
    {
        return ByteBuffer.wrap(block).getLong();
    }

    public static byte[] removeTimestamp(byte[] block)
    {
        ByteBuffer newBlock = ByteBuffer.allocate(block.length - 8);
        newBlock.put(block, 8, block.length - 8);

        return newBlock.array();
    }

    public static int piFunction(int i, int j, int d)
    {
        int ret = j * d + (d-1-i);

        return ret;
    }

    /*
    Credits: https://stackoverflow.com/questions/14485873/audio-change-volume-of-samples-in-byte-array
     */
    public static byte[] reduceAmplitude(byte[] input)
    {
        byte[] array = new byte[input.length];
        for (int i = 0; i < array.length; i+=2)
        {
            short bufferOne = input[i + 1];
            short bufferTwo = input[i];

            bufferOne = (short) ((bufferOne & 0xff) << 8);
            bufferTwo = (short) (bufferTwo & 0xff);

            short res = (short) (bufferOne | bufferTwo);

            res = (short) (res * 0.5);

            array[i] = (byte) res;
            array[i + 1] = (byte) (res >> 8);
        }

        return array;
    }
}
