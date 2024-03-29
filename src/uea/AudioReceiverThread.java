package uea;

import CMPC3M06.AudioPlayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AudioReceiverThread implements Runnable{

    static DatagramSocket2 receiving_socket;
    private final int port;
    private final int blockSize;

    public AudioReceiverThread(int port, int blockSize)
    {
        this.port = port;
        this.blockSize = blockSize;
    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run (){

        //Open a socket to receive from on port PORT
        try{
            receiving_socket = new DatagramSocket2(this.port);
        } catch (SocketException e){
            System.out.println("ERROR: " + getClass().getSimpleName() + ": Could not open UDP socket to receive from.");
            e.printStackTrace();
            System.exit(0);
        }

        //Setup audio player
        AudioPlayer audioPlayer = null;
        try
        {
            audioPlayer = new AudioPlayer();
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
            System.exit(0);
        }

        //Main loop.
        boolean running = true;

        int currentBlock = 0;

        Map<Integer, byte[]> packetStore = new HashMap<>();
        Map<Integer, Long> packetTimes = new HashMap<>();
        byte[] previousData = null;
        byte[] noData = ByteBuffer.allocate(512).array();

        while (running){
            try{
                //Receive a DatagramPacket (note that the string cant be more than 80 chars)
                byte[] buffer = new byte[524];
                DatagramPacket packet = new DatagramPacket(buffer, 0, 524);

                //Receive the packet
                receiving_socket.receive(packet);

                int id = Utils.readId(packet.getData());
                packet.setData(Utils.removeId(packet.getData()));

                long time = Utils.readTimestamp(packet.getData());
                packet.setData(Utils.removeTimestamp(packet.getData()));
                packetTimes.put(id, time);

                packetStore.put(id, packet.getData());
                //System.out.println("Receiving Packet ID: " + id + ".");

                if (id >= currentBlock + (blockSize * blockSize)) // Assume the current block is done
                {
                    for (int i = currentBlock; i < currentBlock + (blockSize * blockSize); i++)
                    {
                        byte[] data = packetStore.remove(i);

                        if (data != null)
                        {
                            audioPlayer.playBlock(data);
                            previousData = data;
                            System.out.println("Playing Packet ID: " + i + ". Took: " + ((System.nanoTime() - packetTimes.get(i)) / 1_000_000_000.0) + "s");
                        }
                        else if (previousData != null)
                        {
                            audioPlayer.playBlock(noData);
                            System.out.println("Skipping Packet ID: " + i + ".");
                        }
                        else
                        {
                            //Something, the very first packet was lost..
                            System.out.println("First packet was lost?");
                        }
                    }

                    currentBlock += (blockSize * blockSize);
                }
            } catch (IOException e){
                System.out.println("ERROR: " + getClass().getSimpleName() + ": Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        receiving_socket.close();
    }
}
