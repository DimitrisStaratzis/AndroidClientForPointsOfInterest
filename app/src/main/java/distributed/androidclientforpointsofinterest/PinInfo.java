package distributed.androidclientforpointsofinterest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import distributed.POIS;

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
        final ImageView image  = findViewById(R.id.image);
        final TextView name =findViewById(R.id.nameValue);
        final TextView category = findViewById(R.id.categoryValue);
        final TextView distance = findViewById(R.id.distanceValue);
        DecimalFormat numberFormat = new DecimalFormat("#.0");
        distance.setText(numberFormat.format((MainActivity.poisInfo[index].getDistance()))+"km");
        name.setText(MainActivity.poisInfo[index].getCategory());
        category.setText(MainActivity.poisInfo[index].getName());
        AsyncTaskRunner runner = new AsyncTaskRunner(image);
        try{
            runner.execute(MainActivity.poisInfo[index].getPhotosLink());
        }catch(Exception e)
        {
            System.out.println("thread error");
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, Bitmap>
    {
        Bitmap bitmap = null;
        ImageView imageView;

        public AsyncTaskRunner(ImageView imageView)
        {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {


            String url=params[0];
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

    }
}


