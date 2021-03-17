package uea.ds3;

import CMPC3M06.AudioPlayer;
import uea.Utils;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DS3Receiver implements Runnable{

    static DatagramSocket3 receiving_socket;
    private final int port;

    public DS3Receiver(int port)
    {
        this.port = port;
    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run (){

        //Open a socket to receive from on port PORT
        try{
            receiving_socket = new DatagramSocket3(this.port);
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

                if (id >= currentBlock + 14) // Assume the current block is done
                {
                    ByteBuffer holder = ByteBuffer.allocate(512);

                    for (int i = currentBlock; i < currentBlock + 14; i++)
                    {
                        byte[] data = packetStore.remove(i);

                        if (data != null)
                        {
                            holder.put(data, 0, 256);

                            previousData = Utils.reduceAmplitude(data);
                        }
                        else if (previousData != null)
                        {
                            holder.put(previousData, 0, 256);
                            previousData = Utils.reduceAmplitude(previousData);
                        }
                        else
                        {
                            //Something, the very first packet was lost..
                            System.out.println("First packet was lost?");
                        }

                        if (i % 2 == 1)
                        {
                            audioPlayer.playBlock(holder.array());
                            long lastTime = 0L;
                            if (packetTimes.get(i) == null)
                            {
                                if (packetTimes.get(i - 1) != null)
                                {
                                    lastTime = packetTimes.get(i -1);
                                }
                            }
                            else
                            {
                                lastTime = packetTimes.get(i);
                            }

                            if (lastTime == 0L)
                            {
                                System.out.println("Playing Packet IDs: " + i + " & " + (i -1) + ". (Repeat)");
                            }
                            else
                            {
                                System.out.println("Playing Packet IDs: " + i + " & " + (i -1) + ". Took: " + ((System.nanoTime() - lastTime) / 1_000_000_000.0) + "s");
                            }

                            holder = ByteBuffer.allocate(512);
                        }
                    }

                    currentBlock += 14;
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
