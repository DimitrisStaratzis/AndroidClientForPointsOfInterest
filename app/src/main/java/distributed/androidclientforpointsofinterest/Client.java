/**
 * Created by
 * Marios Prokopakis(3150141)
 * Stratos Xenouleas(3150130)
 * Foivos Kouroutsalidis(3080250)
 * Dimitris Staratzis(3150166)
 */
package distributed.androidclientforpointsofinterest;

import android.content.Intent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class Client
{

    /*public static void main(String args[])
    {
        new Client().connectToMaster();
    }*/

    /**
     * This method connects to Master using a requestSocket
     */
    public Integer[] connectToMaster(String user, final String k)
    {
        Integer[] topK;
        ClientThread thread = new ClientThread(user, k);
        Thread t = new Thread(thread);
        t.start();

        try{
            t.join();
        }catch(Exception e)
        {

        }
        topK = thread.getTopKIndexes();
        return topK;


    }
}
