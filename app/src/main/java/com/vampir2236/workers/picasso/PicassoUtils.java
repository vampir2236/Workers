package com.vampir2236.workers.picasso;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PicassoUtils {
    private static final String TAG = "PicassoUtils";

    public static void loadImage(Context context, String url,
                          int placeholder, ImageView imageView) {
        try {
            if (url == null || url.isEmpty()) {
                Picasso.with(context)
                        .load(placeholder)
                        .resize(60, 60)
                        .centerCrop()
                        .into(imageView);
            } else {
                Picasso.with(context)
                        .load(url)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .resize(60, 60)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(imageView);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
