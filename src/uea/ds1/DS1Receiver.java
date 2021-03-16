package uea.ds1;

import CMPC3M06.AudioPlayer;
import uea.Utils;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DS1Receiver implements Runnable{

    static DatagramSocket2 receiving_socket;
    private final int port;

    public DS1Receiver(int port)
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

        while (running){
            try{
                //Receive a DatagramPacket (note that the string cant be more than 80 chars)
                byte[] buffer = new byte[512];
                DatagramPacket packet = new DatagramPacket(buffer, 0, 512);

                //Receive the packet
                receiving_socket.receive(packet);

                audioPlayer.playBlock(packet.getData());

            } catch (IOException e){
                System.out.println("ERROR: " + getClass().getSimpleName() + ": Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        receiving_socket.close();
    }
}
