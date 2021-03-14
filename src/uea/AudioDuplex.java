import uk.ac.uea.cmp.voip.DatagramSocket2;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class AudioDuplex
{

    public static void main (String[] args){

        /*AudioReceiverThread receiver = new AudioReceiverThread(44444);
        AudioSenderThread sender = new AudioSenderThread(44444, "localhost");

        receiver.start();
        sender.start();*/


        int port = 44444;
        InetSocketAddress address = new InetSocketAddress("localhost",port);
        DatagramSocket socket;

        try{
            socket = new DatagramSocket2(44444);

            new AudioSenderThread(socket,address,port).start();
            new AudioReceiverThread(socket,port).start();

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}