package distributed.androidclientforpointsofinterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.widget.TextView;

public class PinInfo extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_info);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        int index = extras.getInt("pinIndex");
        TextView name =findViewById(R.id.nameValue);
        TextView category = findViewById(R.id.categoryValue);
        TextView link =findViewById(R.id.photosLinkValue);
        name.setText(MainActivity.poisInfo[index].getName());
        category.setText(MainActivity.poisInfo[index].getCategory());
        link.setText(MainActivity.poisInfo[index].getPhotosLink());
    }
}
