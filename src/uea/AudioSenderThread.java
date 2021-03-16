package uea;
import CMPC3M06.AudioRecorder;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AudioSenderThread implements Runnable{

    static DatagramSocket2 sending_socket;
    private final int port;
    private final String address;
    private final int blockSize;

    public AudioSenderThread(int port, String address, int blockSize)
    {
        this.port = port;
        this.address = address;
        this.blockSize = blockSize;
    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run (){

        //IP ADDRESS to send to
        InetAddress clientIP = null;
        try {
            clientIP = InetAddress.getByName(this.address);
        } catch (UnknownHostException e) {
            System.out.println("ERROR: " + getClass().getSimpleName() + ": Could not find client IP");
            e.printStackTrace();
            System.exit(0);
        }


        //Open a socket to send from
        //We dont need to know its port number as we never send anything to it.
        //We need the try and catch block to make sure no errors occur.
        try{
            sending_socket = new DatagramSocket2();
        } catch (SocketException e){
            System.out.println("ERROR: " + getClass().getSimpleName() + ": Could not open UDP socket to send from.");
            e.printStackTrace();
            System.exit(0);
        }

        //Setup audio recorder
        AudioRecorder audioRecorder = null;
        try {
            audioRecorder = new AudioRecorder();
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
            System.exit(0);
        }


        //Main loop
        boolean running = true;

        //Id of packets
        int id = 0;
        int blocksSent = 0;

        Map<Integer, DatagramPacket> packetStore = new HashMap<>();

        while (running){
            try{
                //Convert the audio recorder block into an array of bytes
                //Size is 512
                byte[] audioOriginal = audioRecorder.getBlock();
                ByteBuffer audioFirst = ByteBuffer.allocate(256).put(audioOriginal, 0, 256);
                ByteBuffer audioSecond = ByteBuffer.allocate(256).put(audioOriginal, 256, 256);

                byte[] firstData = Utils.applyId(Utils.applyTimestamp(audioFirst.array(), System.nanoTime()), id++);
                DatagramPacket firstPacket = new DatagramPacket(firstData, firstData.length, clientIP, this.port);

                packetStore.put(id -1, firstPacket);

                byte[] secondData = Utils.applyId(Utils.applyTimestamp(audioSecond.array(), System.nanoTime()), id++);
                DatagramPacket secondPacket = new DatagramPacket(secondData, firstData.length, clientIP, this.port);

                packetStore.put(id -1, secondPacket);

                //Send it
                if (id != 0 && id % (blockSize*blockSize) == 0)
                {
                    for (int i = 0; i < blockSize; i++)
                    {
                        for (int j = 0; j < blockSize; j++)
                        {
                            int localId = (blocksSent * (blockSize*blockSize)) + Utils.piFunction(i, j, blockSize);
                            sending_socket.send(packetStore.remove(localId));
                        }
                    }

                    blocksSent++;
                }

            } catch (IOException e){
                System.out.println("ERROR: " + getClass().getSimpleName() + ": Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        sending_socket.close();
    }
}
