package uea;

public class DS2Analysis
{
    public static void main(String[] args)
    {
        int largestLoss = 0;
        int largestSend = 0;

        int currentSend = 0;
        int currentLoss = 0;
        int last = 0;

        remapPloss(4);

        for (int i = 0; i < ploss.length;)
        {
            i++;

            if (i== ploss.length)
            {
                break;
            }

            if (ploss[i] == -1)
            {
                //Don't send
                if (ploss[i] != last)
                {
                    System.out.println(currentSend + " Sent packets");
                    if (currentSend > largestSend)
                    {
                        largestSend = currentSend;
                    }
                    currentLoss = 0;
                    currentSend = 0;
                    last = ploss[i];
                }

                currentLoss++;
            }
            else
            {
                //Send
                if (ploss[i] != last)
                {
                    System.out.println(currentLoss + " Lost packets");
                    if (currentLoss > largestLoss)
                    {
                        largestLoss = currentLoss;
                    }
                    currentLoss = 0;
                    currentSend = 0;
                    last = ploss[i];
                }

                currentSend++;
            }
        }

        //TODO Handle the first ploss

        if (ploss[0] == -1)
        {
            //Don't send
            if (ploss[0] != last)
            {
                System.out.println(currentSend + " Sent packets");
                currentLoss = 0;
                currentSend = 0;
                last = ploss[0];
            }

            currentLoss++;
        }
        else
        {
            //Send
            if (ploss[0] != last)
            {
                System.out.println(currentLoss + " Lost packets");
                currentLoss = 0;
                currentSend = 0;
                last = ploss[0];
            }

            currentSend++;
        }

        if (currentLoss > 1)
        {
            System.out.println(currentLoss + " Lost packets");
        }

        if (currentSend > 1)
        {
            System.out.println(currentSend + " Sent packets");
        }

        System.out.println("Largest packet loss burst: " + largestLoss);
        System.out.println("Largest packet send burst: " + largestLoss);
    }

    public static void remapPloss(int d)
    {
        int[] newPloss = new int[ploss.length];

        System.out.println(ploss.length);

        for (int part = 0; part < ploss.length;)
        {
            for (int i = 0; i < 4; i++) //From 0-3
            {
                for (int j = 0; j < 4; j++) //From 0-3
                {
                    newPloss[(i * d) + j + part] = ploss[Utils.piFunction(i, j, d) + part];
                    //System.out.println("Setting newPloss: " + ((i * d) + j + part) + " to ploss[" + (Utils.piFunction(i, j, d) + part) + "] which is " + ploss[Utils.piFunction(i, j, d) + part] + " part = " + part);
                }
            }

            part = part + 16;
        }

        ploss = newPloss;
    }

    static int[] ploss = new int[] {
            0, 0, -1, -1, 0, -1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, -1, -1, 0,
            0, 0, 0, 0, 0, -1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, -1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
            0, -1, -1, -1, -1, -1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, -1, -1, -1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, 0, 0, 0, -1, -1, -1,
            -1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -1, -1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -1, -1, -1, -1, -1, -1,
            -1, 0, 0, 0, 0, 0, -1, -1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, 0, 0, 0, 0,
            0, -1, -1, 0, 0, 0, 0, 0, -1, -1,
            -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, 0, 0,
            0, 0, 0, 0, 0, -1, 0, 0, 0, 0,
            0, -1, -1, 0, 0, 0, 0, 0, 0, -1,
            -1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, -1, -1, -1, -1, -1, -1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, -1, -1,
            -1, -1, 0, 0, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
            -1, -1, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -1, -1, -1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, -1, -1, -1,
            -1, 0, 0, 0, 0, 0, 0, 0, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
            0, -1, -1, -1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
            -1, -1, -1, -1, -1, -1, -1, 0, 0, 0,
            0, 0, 0, -1, 0, 0, 0, 0, -1, -1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, -1, -1, 0, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, -1, -1,
            -1, -1, -1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, -1,
            -1, -1, -1, -1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, -1, -1, -1, 0, 0, 0,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, -1, -1, -1, -1, -1, -1, -1,
            -1, 0, 0, -1, -1, -1, -1, -1, -1, -1,
            -1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, -1, -1, -1, -1, -1,
            -1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, -1, -1, -1, 0, 0, 0, 0, 0,
            0, 0, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0 };
}
