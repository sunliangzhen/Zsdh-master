package com.toocms.dink5.mylibrary.commonutils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ImageUtils {

    public static void display(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

}
