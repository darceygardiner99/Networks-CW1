package uea;
/*
 * TextDuplex.java
 */

/**
 *
 * @author  abj
 */
public class AudioDuplex
{

    public static void main (String[] args){

        AudioReceiverThread receiver = new AudioReceiverThread(44444);
        AudioSenderThread sender = new AudioSenderThread(44444, "localhost");

        receiver.start();
        sender.start();

    }

}
