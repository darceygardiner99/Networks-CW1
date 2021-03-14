package uea;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils
{
    public static void main(String[] args)
    {
        byte[] buff = {0, 0, 0, 0};

        byte[] withId = applyId(buff, 1);

        System.out.println(readId(withId));

        byte[] withoutId = removeId(withId);

        for (int i = 0; i < buff.length; i++)
        {
            if (buff[i] != withoutId[i])
            {
                System.out.println("Error at " + i);
            }
        }

        System.out.println("");

    }

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
}
