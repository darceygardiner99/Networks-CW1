package uea.ds3;

import java.util.Vector;

public class DS3Analysis
{
    public static void main(String[] args)
    {
        /*
            -1 = Packet Loss
            0 = Becomes 0
            1 = Becomes 1
            2 = Becomes 1
            3 = Becomes 1
         */

        Vector queue = new Vector();
        boolean packets_can_send = false;
        int packets_stored = 0;

        int previous_id = 0;

        for (int loss_count = 0; loss_count < ploss.length;)
        {
            loss_count++;

            int i = ploss[loss_count];
            if (i != -1)
            {
                queue.add(loss_count - 1);
                if (!packets_can_send && packets_stored < 2)
                    packets_stored++;
                if (!packets_can_send && packets_stored >= 2)
                    packets_can_send = true;
                if (i > 0) {
                    i = 1;
                } else {
                    i = 0;
                }

                if (packets_can_send)
                {
                    int current_id = (int) queue.elementAt(i);
                    System.out.println("Sending packet with id: " + queue.elementAt(i));
                    queue.removeElementAt(i);
                    //System.out.println("Differences in ID: " + (Math.abs(current_id - previous_id)));
                    previous_id = current_id;
                }
            }
            else
            {
                System.out.println("Lost packet with id: " + (loss_count - 1));
            }

        }
    }

    static int[] ploss = new int[] {
            0, 0, 0, 0, 0, 2, 0, 0, 3, 0,
            1, 0, 0, 2, 0, 0, 1, 0, 0, 2,
            0, 1, 0, 3, 1, -1, 1, 0, 2, 1,
            0, 0, -1, -1, -1, -1, 0, 1, 0, 0,
            1, 0, 0, 1, -1, 1, 3, 1, 0, 0,
            1, 0, -1, 0, 1, 0, 1, 2, -1, 0,
            2, 1, -1, 0, 1, 0, -1, 2, 1, 0,
            -1, 1, 0, 0, 1, 0, 1, 0, -1, 1,
            2, 0, 1, 0, -1, 0, 1, 3, 1, 0,
            0, -1, 0, 1, 0, 0, 1, 2, 1, 0,
            1, -1, 1, 0, -1, 0, 1, 0, 2, 0,
            3, 0, -1, 1, -1, 1, 0, 1, 0, 1,
            2, -1, 0, 0, 3, 0, 0, 1, 1, 1,
            -1, -1, 1, 0, 1, 0, 1, 0, 0, 0,
            -1, 0, 1, 0, 3, 0, 0, 1, 0, 1,
            -1, 1, 1, 1, 0, 0, 1, 0, 1, -1,
            2, 1, 0, 1, 1, 1, 1, 2, 0, 1,
            3, 1, 0, -1, 1, 1, 0, 1, 0, 1,
            0, 1, -1, 1, 0, -1, 0, 0, 1, 0,
            -1, 0, 1, 3, 1, 1, 2, 0, -1, 1,
            1, 2, 0, 0, -1, 0, 1, -1, 0, -1,
            3, 1, 0, 3, 0, 1, 0, 1, 0, 1,
            -1, -1, 0, 1, 0, 0, 1, 2, 0, -1,
            0, 1, 3, 1, 0, 1, 0, 2, 0, -1,
            0, -1, 2, 0, 2, 0, 1, 2, 0, 1,
            0, -1, -1, 0, -1, 0, 2, 1, 0, 0,
            1, 0, 0, -1, 2, 0, 0, 2, 0, 1,
            2, 1, -1, 0, -1, 0, 1, 0, 0, 1,
            0, -1, 0, 1, 0, 0, 1, -1, 0, 0,
            2, 1, -1, 0, 1, 0, 1, 0, -1, 1,
            1, 0, 1, -1, 2, 1, 0, 0, 0, 1,
            0, -1, 0, -1, 0, 1, 0, 1, 0, -1,
            -1, -1, 1, 0, 1, 0, 1, -1, 0, 1,
            0, -1, 0, 0, 1, 0, 1, 1, 1, 1,
            0, -1, 1, 0, 0, 1, 0, 2, 1, 0,
            1, 0, 1, 2, -1, 1, 0, 1, 0, 1,
            0, 0, 1, 0, 1, -1, 0, -1, 1, 0,
            0, -1, -1, 0, -1, 0, 1, 0, 0, 2,
            1, 0, 0, 3, -1, 1, 2, -1, 1 };
}
