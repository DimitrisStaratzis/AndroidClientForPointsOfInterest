package distributed.androidclientforpointsofinterest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

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
                    Client client = new Client();
                    Integer[] topK = client.connectToMaster(userName.getText().toString(), pois.getText().toString());
                    if(topK != null)
                    {
                        for (int i = 0; i<topK.length; i++)
                        {
                            System.out.println(topK[i]);
                        }
                        startActivity(goToMpaps);
                    }else
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Unable to connect.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
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
}
