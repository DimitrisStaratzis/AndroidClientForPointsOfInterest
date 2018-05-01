package distributed.androidclientforpointsofinterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;

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
                Intent goToMpaps = new Intent(MainActivity.this, MapsActivity.class);
                String example = userName.getText().toString() + pois.getText().toString();
                goToMpaps.putExtra("Coordinates", example);//This method send he coordinates to the maps activity
                startActivity(goToMpaps);
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
