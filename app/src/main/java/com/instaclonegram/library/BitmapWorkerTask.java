package com.instaclonegram.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.widget.ImageView;

import com.instaclonegram.adapters.FeedListViewAdapter;
import com.instaclonegram.models.Photo;

import java.lang.ref.WeakReference;

import static com.instaclonegram.adapters.FeedListViewAdapter.addBitmapToMemoryCache;

/**
 * Created by lamine on 14/04/2016.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    public String data = "";
    private int screen_width;
    private int new_photo_height;
    private ImageView imageView;
    private LruCache<String, Bitmap> mMemoryCache;


    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageView = imageView;
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];
        new_photo_height = Integer.valueOf(params[1]);
        screen_width = Integer.valueOf(params[2]);

        byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        addBitmapToMemoryCache(String.valueOf(params[3]), decodedByte);
        //Bitmap dec = Bitmap.createScaledBitmap(decodedByte, screen_width, new_photo_height, true);
        return decodedByte;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setMinimumWidth(screen_width);
                imageView.setMinimumHeight(new_photo_height);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}