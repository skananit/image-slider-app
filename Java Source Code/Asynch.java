package ca.uwo.eng.se3313.lab2;

/**
 * Created by Shima on 2017-11-14.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Asynch extends AsyncTask<Void, Void, Bitmap> {
    private String urls;
    private IImageDownloader.SuccessHandler successHandler;

    public Asynch(String u, IImageDownloader.SuccessHandler success) {
        urls = new String(u);
        successHandler = success;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitMap;
        try {

            //Pass it the list of urls
            URL urlList = new URL(urls);

            //New connection
            HttpURLConnection connection = (HttpURLConnection) urlList.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.connect();

            //Download the image as a bitMap
            InputStream input = connection.getInputStream();
            bitMap = BitmapFactory.decodeStream(input);

            //Return the image as a bitMap
            return bitMap;

        }
        catch (IOException e) {

            e.printStackTrace();
            return null;

        }

    }


    protected void onPostExecute(Bitmap bitmap) {

        //calls the success handler with the bitmap as an argument
        successHandler.onComplete(bitmap);

    }
}
