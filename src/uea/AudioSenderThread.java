package uea;
import CMPC3M06.AudioRecorder;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        int blocksSent = 0;

        Map<Integer, DatagramPacket> packetStore = new HashMap<>();

        while (running){
            try{
                //Convert the audio recorder block into an array of bytes
                //Size is 512
                byte[] audio = audioRecorder.getBlock();
                byte[] timeStamp = Utils.applyTimestamp(audio, System.currentTimeMillis());
                byte[] data = Utils.applyId(timeStamp, id++);


                //Make a DatagramPacket from it, with client address and port number
                DatagramPacket packet = new DatagramPacket(data, data.length, clientIP, this.port);

                /*System.out.println("Sending Packet ID: " + (id -1) + " - " + Arrays.toString(audio));*/

                packetStore.put(id -1, packet);

                //Send it
                if (id != 0 && id % 16 == 0)
                {
                    for (int i = 0; i < 4; i++)
                    {
                        for (int j = 0; j < 4; j++)
                        {
                            int localId = (blocksSent * 16) + Utils.piFunction(i, j, 4);
                            sending_socket.send(packetStore.remove(localId));

                            //System.out.println("Sending Packet ID: " + localId + ".");
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
