package distributed.androidclientforpointsofinterest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import distributed.POIS;

public class MainActivity extends AppCompatActivity
{
    public static Integer[] topKIndexes;
    public static POIS[] poisInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button viewOnMap = (Button)findViewById(R.id.goToMapButton);
        final EditText userName = (EditText)findViewById(R.id.User);
        final EditText pois = (EditText)findViewById(R.id.poisReqested);
        userName.setAlpha(0.5f);
        pois.setAlpha(0.5f);

        viewOnMap.setOnClickListener(new View.OnClickListener()
        {
            /**
             * This click listener performs the transition from the MainActivity to the MapsActivity.
             * @param view
             */
            @Override
            public void onClick(View view)
            {

                if(!userName.getText().toString().equals("") && !pois.getText().toString().equals(""))
                {
                    Intent goToMpaps = new Intent(MainActivity.this, MapsActivity.class);
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    try{
                        poisInfo = runner.execute(userName.getText().toString(), pois.getText().toString()).get();
                    }catch(Exception e)
                    {

                    }
                    poisInfo = runner.getPoisInfo();
                    topKIndexes = runner.getTopKIndexes();
                    startActivity(goToMpaps);
                    /*if(topK != null)
                    {
                        startActivity(goToMpaps);
                    }else
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Unable to connect.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }*/
                }else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Please complete all the fields";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });


        /**
         * This focus change listener makes the selection of an edit text visible
         */
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    userName.setAlpha(1);
                }else
                {
                    userName.setAlpha(0.5f);
                }
            }
        });

        /**
         * This focus change listener makes the selection of an edit text visible
         */
        pois.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    pois.setAlpha(1);
                }else
                {
                    pois.setAlpha(0.5f);
                }
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, POIS[]>
    {
        ProgressDialog progress;
        public Integer[] topKIndexes;
        public POIS[] poisInfo;

        public Integer[] getTopKIndexes()
        {
            return topKIndexes;
        }

        public POIS[] getPoisInfo()
        {
            return poisInfo;
        }


        @Override
        protected POIS[] doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            String user  = params[0];
            String pois = params[1];

            Socket requestSocket = null;
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try
            {
                /* Create socket for contacting the server on port 7777*/
                requestSocket = new Socket("169.254.33.44", 7777);
                if(requestSocket.isConnected())
                {
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    in = new ObjectInputStream(requestSocket.getInputStream());

                    out.writeObject(pois+";"+user);
                    out.flush();
                    topKIndexes = (Integer[])in.readObject();
                    poisInfo = (POIS[])in.readObject();
                    for (int i = 0; i<topKIndexes.length; i++)
                    {
                        System.out.println(topKIndexes[i] + " KENO " +poisInfo[i]);
                    }
                }
                else
                {
                    System.out.println("MALAKAS");
                }


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
            return poisInfo;

        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);


        }

        @Override
        protected void onProgressUpdate(String... text) {
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.show();
        }

    }
}
