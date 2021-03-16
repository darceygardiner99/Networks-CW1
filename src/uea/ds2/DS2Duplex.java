package uea.ds2;
/*
 * TextDuplex.java
 */

/**
 *
 * @author  abj
 */
public class DS2Duplex
{

    public static void main (String[] args){

        AudioReceiverThread receiver = new AudioReceiverThread(44444, 4);
        AudioSenderThread sender = new AudioSenderThread(44444, "localhost", 4);

        receiver.start();
        sender.start();

    }

}
