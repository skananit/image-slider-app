package ca.uwo.eng.se3313.lab2;



/**
 * Created by Shima on 2017-11-14.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class imageDownloader implements IImageDownloader {

    /**
     * <b>Asynchronously</b> downloads an image from the imageUrl passed.
     *
     * The suggested implementation will use {@link AsyncTask} to run download code. Any
     * exceptions occuring will utilize an {@link ErrorHandler} instance to have the calling code
     * handle errors.
     *
     * Resources should not be leaked and must be closed. The
     * <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
     * statement to save some effort closing resources (also {@link java.io.Closeable}).
     *
     * @param imageUrl String URL to download from.
     * @param handler Code to execute in the UI thread on success (accepts a {@link Bitmap}).
     *
     * @throws IllegalArgumentException if imageUrl is not a valid {@link java.net.URL}, delegated
     *                                  from {@link java.net.MalformedURLException}.
     */
    @Override
    public void download(@NonNull String imageUrl, @NonNull SuccessHandler handler) { //creates a new AsyncTask and passes the imageUrl and Success handler to it
        Asynch task = new Asynch(imageUrl, handler);
        task.execute();

    }

}

