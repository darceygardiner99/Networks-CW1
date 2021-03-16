package uea.ds1;

import CMPC3M06.AudioRecorder;
import uea.Utils;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DS1Sender implements Runnable{

    static DatagramSocket2 sending_socket;
    private final int port;
    private final String address;

    public DS1Sender(int port, String address)
    {
        this.port = port;
        this.address = address;
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

        while (running){
            try{
                //Convert the audio recorder block into an array of bytes
                //Size is 512
                byte[] audioOriginal = audioRecorder.getBlock();

                sending_socket.send(new DatagramPacket(audioOriginal, audioOriginal.length, clientIP, this.port));

            } catch (IOException e){
                System.out.println("ERROR: " + getClass().getSimpleName() + ": Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        sending_socket.close();
    }
}
