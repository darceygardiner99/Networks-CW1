package uea;
import CMPC3M06.AudioRecorder;
import javafx.util.Pair;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;

public class AudioSenderThread implements Runnable{

    static DatagramSocket2 sending_socket;
    private final int port;
    private final String address;

    public AudioSenderThread(int port, String address)
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

        //Id of packets
        int id = 0;

        while (running){
            try{
                //Convert the audio recorder block into an array of bytes
                //Size is 512
                byte[] audio = audioRecorder.getBlock();
                byte[] data = Utils.applyId(audio, id++);


                //Make a DatagramPacket from it, with client address and port number
                DatagramPacket packet = new DatagramPacket(data, data.length, clientIP, this.port);

                System.out.println("Sending Packet ID: " + (id -1) + " - " + Arrays.toString(audio));

                //Send it
                sending_socket.send(packet);

            } catch (IOException e){
                System.out.println("ERROR: " + getClass().getSimpleName() + ": Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        sending_socket.close();
    }
}
