package uea.ds3;

import uea.AudioReceiverThread;
import uea.AudioSenderThread;

public class DS3Duplex
{
    public static void main (String[] args){

        DS3Receiver receiver = new DS3Receiver(44444);
        DS3Sender sender = new DS3Sender(44444, "localhost");

        receiver.start();
        sender.start();

    }
}
