import CMPC3M06.AudioRecorder;

import javax.sound.sampled.LineUnavailableException;
import java.net.*;
import java.io.*;
import java.util.Vector;

public class AudioSenderThread implements Runnable{

    static DatagramSocket sending_socket;
    private final int port;
    private final InetSocketAddress address;

    /*public AudioSenderThread(int port, String address)
    {
        this.port = port;
        this.address = address;
    }*/

    public AudioSenderThread(DatagramSocket socket, InetSocketAddress address, int port){
        sending_socket = socket;
        this.address = address;
        this.port = port;
    }

    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run (){

        //IP ADDRESS to send to
        InetAddress clientIP = null;
        try {
            clientIP = InetAddress.getByName(String.valueOf(this.address));
        } catch (UnknownHostException e) {
            System.out.println("ERROR: " + getClass().getSimpleName() + ": Could not find client IP");
            e.printStackTrace();
            System.exit(0);
        }


        //Open a socket to send from
        //We dont need to know its port number as we never send anything to it.
        //We need the try and catch block to make sure no errors occur.
        try{
            sending_socket = new DatagramSocket();
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
                byte[] buffer = audioRecorder.getBlock();

                //Make a DatagramPacket from it, with client address and port number
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientIP, this.port);

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