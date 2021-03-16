package uea.ds1;
/*
 * TextDuplex.java
 */

import uea.ds2.AudioReceiverThread;
import uea.ds2.AudioSenderThread;

/**
 *
 * @author  abj
 */
public class DS1Duplex
{

    public static void main (String[] args){

        DS1Receiver receiver = new DS1Receiver(44444);
        DS1Sender sender = new DS1Sender(44444, "localhost");

        receiver.start();
        sender.start();

    }

}
