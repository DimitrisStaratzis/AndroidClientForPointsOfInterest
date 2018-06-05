package distributed.androidclientforpointsofinterest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import distributed.POIS;

public class MainActivity extends AppCompatActivity
{
    static int kilometers;
    public static Integer[] topKIndexes;
    public static POIS[] poisInfo;
    public static String ipAddress;
    public static POIS locationPoi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button viewOnMap = findViewById(R.id.goToMapButton);
        final EditText userName = findViewById(R.id.User);
        final EditText pois = findViewById(R.id.poisReqested);
        final EditText ip = findViewById(R.id.ipEditText);
        final SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView progressText = findViewById(R.id.progress);
        final EditText lat = findViewById(R.id.lat);
        final EditText longt =findViewById(R.id.longt);
        final Spinner catSpinner = findViewById(R.id.cat_spinner);
        userName.setAlpha(0.5f);
        pois.setAlpha(0.5f);
        ip.setAlpha(0.5f);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        catSpinner.setAdapter(adapter);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                kilometers=progress;
                progressText.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        viewOnMap.setOnClickListener(new View.OnClickListener()
        {
            /**
             * This click listener performs the transition from the MainActivity to the MapsActivity.
             * @param view
             */
            @Override
            public void onClick(View view)
            {

                if(!userName.getText().toString().equals("") && !pois.getText().toString().equals("") && !ip.getText().toString().equals(""))
                {
                    int category = catSpinner.getSelectedItemPosition();
                    ipAddress=ip.getText().toString();
                    Intent goToMpaps = new Intent(MainActivity.this, MapsActivity.class);
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    try{
                        poisInfo = runner.execute(userName.getText().toString(), pois.getText().toString(), lat.getText().toString(), longt.getText().toString(), kilometers+"", category+"").get(5000, TimeUnit.MILLISECONDS);
                    }catch(Exception e)
                    {

                    }
                    poisInfo = runner.getPoisInfo();
                    topKIndexes = runner.getTopKIndexes();
                    locationPoi = runner.getLocationPoi();
                    if(poisInfo!=null && topKIndexes!=null)
                    {
                        startActivity(goToMpaps);
                    }else
                    {
                        Toast.makeText(MainActivity.this, "Unable to connect with server", Toast.LENGTH_LONG).show();
                    }

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

        ip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    ip.setAlpha(1);
                }else
                {
                    ip.setAlpha(0.5f);
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
        public Integer[] topKIndexes;
        public POIS[] poisInfo;
        public POIS locationPoi;

        public Integer[] getTopKIndexes()
        {
            return topKIndexes;
        }

        public POIS[] getPoisInfo()
        {
            return poisInfo;
        }

        public POIS getLocationPoi() {
            return locationPoi;
        }

        @Override
        protected POIS[] doInBackground(String... params) {
            String user  = params[0];
            String pois = params[1];
            String lat = params[2];
            String longt = params[3];
            String kilometers = params[4];
            String category =params[5];

            Socket requestSocket = null;
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try
            {
                /* Create socket for contacting the server on port 4200*/
                requestSocket = new Socket(ipAddress, 4200);
                if(requestSocket.isConnected())
                {
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    in = new ObjectInputStream(requestSocket.getInputStream());

                    out.writeObject(pois+";"+user+";"+lat+";"+longt+";"+kilometers+";"+category);
                    out.flush();
                    topKIndexes = (Integer[])in.readObject();
                    poisInfo = (POIS[])in.readObject();
                    locationPoi = (POIS)in.readObject();
                    for (int i = 0; i<topKIndexes.length; i++)
                    {
                        System.out.println(topKIndexes[i] + "  " +poisInfo[i]);
                    }
                }
                else
                {
                    System.out.println("error");
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
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

    }
}
