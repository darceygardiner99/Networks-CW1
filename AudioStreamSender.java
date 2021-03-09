import CMPC3M06.AudioRecorder;

import java.net.*;
import java.util.Vector;

public class AudioStreamSender {
    static DatagramSocket sending_socket;

    public static void main (String[] args) throws Exception{

        /*---------------Create Audio Recorder---------------*/

        //Vector used to store audio blocks (32ms/512bytes each)
        //Not sure if I need this because the audio blocks go straight to the Datagram packet?
        Vector<byte[]> voiceVector = new Vector<byte[]>();

        //Initialise AudioPlayer and AudioRecorder objects
        AudioRecorder recorder = new AudioRecorder();

        //Recording time in seconds
        int recordTime = 5;

        /*---------------Open a sending socket---------------*/

        //Port to send to
        int PORT = 55555;

        //IP ADDRESS to send to
        InetAddress clientIP = null;
        try {
            clientIP = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.out.println("ERROR: AudioStreamSender: Could not find client IP");
            e.printStackTrace();
            System.exit(0);
        }

        //Open a socket to send from
        DatagramSocket sending_socket = null;
        try{
            sending_socket = new DatagramSocket();
        } catch (SocketException e){
            System.out.println("ERROR: AudioStreamSender: Could not open UDP socket to send from.");
            e.printStackTrace();
            System.exit(0);
        }

        /*---------------Main while loop---------------*/

        boolean running = true;

        while (running){
            System.out.println("Recording Audio...");
            for (int i = 0; i < Math.ceil(recordTime / 0.032); i++) {
                byte[] block = recorder.getBlock();
                //voiceVector.add(block); I don't think I need this line as it's going straight into the Datagram packet?
                DatagramPacket packet = new DatagramPacket(block, block.length, clientIP, PORT);
                sending_socket.send(packet);
            }
            recorder.close();
        }
    }
}