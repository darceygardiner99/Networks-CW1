import CMPC3M06.AudioPlayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;


public class AudioStreamReceiver{

    static DatagramSocket receiving_socket;

    public static void main (String[] args) throws Exception {

        /*---------------Create Audio Player---------------*/

        Vector<byte[]> voiceVector = new Vector<byte[]>();

        AudioPlayer player = new AudioPlayer();

        /*---------------Open a receiving socket---------------*/

        int PORT = 55555;

        DatagramSocket receiving_socket = null;
        try{
            receiving_socket = new DatagramSocket(PORT);
        } catch (SocketException e){
            System.out.println("ERROR: AudioStreamReceiver: Could not open UDP socket to receive from.");
            e.printStackTrace();
            System.exit(0);
        }

        /*---------------Main while loop---------------*/

        boolean running = true;

        while (running){

            try{
                //Receive a DatagramPacket (note that the string cant be more than 80 chars)
                byte[] buffer = new byte[1000];
                DatagramPacket packet = new DatagramPacket(buffer, 0, 1000);

                receiving_socket.receive(packet);

                System.out.println("Playing Audio...");

                //Very lost here, should the voiceVector be packet?
                Iterator<byte[]> voiceItr = voiceVector.iterator();
                while (voiceItr.hasNext()) {
                    player.playBlock(voiceItr.next());
                }

                //Close audio output
                player.close();

            } catch (IOException e){
                System.out.println("ERROR: AudioStreamReceiver: Some random IO error occured!");
                e.printStackTrace();
            }



        }
    }

}

