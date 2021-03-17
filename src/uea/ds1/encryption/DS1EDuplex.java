package uea.ds1.encryption;
/*
 * TextDuplex.java
 */

import uea.Utils;

/**
 *
 * @author  abj
 */
public class DS1EDuplex
{

    public static void main (String[] args){

        DS1EReceiver receiver = new DS1EReceiver(44444, true, 92759357);
        DS1ESender sender = new DS1ESender(44444, "localhost", true, 92759357);

        receiver.start();
        sender.start();

    }

}
