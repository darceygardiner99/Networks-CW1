package uea;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils
{
    public static byte[] encrypt(byte[] block, int key)
    {
        ByteBuffer encrypted = ByteBuffer.allocate(block.length);

        ByteBuffer plainTex = ByteBuffer.wrap(block);

        for (int i = 0; i < block.length / 4; i++)
        {
            int fourByte = plainTex.getInt();
            fourByte = fourByte ^ key;

            encrypted.putInt(fourByte);
        }

        return encrypted.array();
    }

    public static byte[] decrypt(byte[] block, int key)
    {
        ByteBuffer decrypted = ByteBuffer.allocate(block.length);

        ByteBuffer cipherText = ByteBuffer.wrap(block);

        for (int i = 0; i < block.length / 4; i++)
        {
            int fourByte = cipherText.getInt();
            fourByte = fourByte ^ key;

            decrypted.putInt(fourByte);
        }

        return decrypted.array();
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
