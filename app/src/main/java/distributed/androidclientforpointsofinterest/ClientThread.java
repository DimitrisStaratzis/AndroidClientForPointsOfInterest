package distributed.androidclientforpointsofinterest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by dimitrisstaratzis on 5/4/18.
 */

public class ClientThread implements Runnable
{
    String user, k;
    Integer[] topKIndexes;
    public ClientThread(String user, String k)
    {
        this.user=user;
        this.k=k;
    }

    public void run(){
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try
        {
            /* Create socket for contacting the server on port 7777*/
            requestSocket = new Socket("169.254.223.171", 7777);
            if(requestSocket.isConnected())
            {
                out = new ObjectOutputStream(requestSocket.getOutputStream());
                in = new ObjectInputStream(requestSocket.getInputStream());

                out.writeObject(user+";"+k);
                out.flush();
                topKIndexes = (Integer[])in.readObject();
            }
            else
            {
                System.out.println("MALAKAS");
            }

            /*for (int i = 0; i<topKIndexes.length; i++)
            {
                System.out.println(topKIndexes[i] + "thread");
            }*/

        } catch (UnknownHostException unknownHost)
        {
            System.err.println("Unknown host");
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        } catch (ClassNotFoundException cnfe)
        {

        } finally
        {
            try
            {
                in.close();
                out.close();
                requestSocket.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public Integer[] getTopKIndexes()
    {
        return topKIndexes;
    }

}