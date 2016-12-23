
package com.luyuan.cleen.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class ImageDownLoader {
    private static ImageDownLoader sInstance;

    public static RequestManager with(Context context) {
        return Glide.with(context);
    }

    public static RequestManager with(Activity activity) {
        return Glide.with(activity);
    }

    public static RequestManager with(FragmentActivity activity) {
        return Glide.with(activity);
    }


    public static RequestManager with(android.app.Fragment fragment) {
        return Glide.with(fragment);
    }

    public static RequestManager with(Fragment fragment) {
        return Glide.with(fragment);
    }

    public static ImageDownLoader get(Context context) {
        if (sInstance == null) {
            sInstance = new ImageDownLoader(context);
        }
        return sInstance;
    }

    private Context mContext;

    private ImageDownLoader(Context context) {
        mContext = context.getApplicationContext();
    }

    public void display(String url, Drawable holderDrawable, Drawable errorDrawable, ImageView imageView) {
        Glide.with(mContext)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderDrawable)
            .error(errorDrawable)
            .into(imageView);
    }

    public void display(String url, Drawable holderDrawable, ImageView imageView) {
        Glide.with(mContext)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderDrawable)
            .into(imageView);
    }

    public void display(String url, @DrawableRes int holderResId, @DrawableRes int errorResId, ImageView imageView) {
        Glide.with(mContext)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderResId)
            .error(errorResId)
            .into(imageView);
    }

    public void display(String url, @DrawableRes int holderResId, ImageView imageView) {
        Glide.with(mContext)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderResId)
            .into(imageView);
    }
    public void display( @DrawableRes int holderResId, ImageView imageView) {
        Glide.with(mContext)
                .load(holderResId)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public void display(File file, Drawable holderDrawable, Drawable errorDrawable, ImageView imageView) {
        Glide.with(mContext)
            .load(file)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderDrawable)
            .error(errorDrawable)
            .into(imageView);
    }

    public void display(File file, Drawable holderDrawable, ImageView imageView) {
        Glide.with(mContext)
            .load(file)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderDrawable)
            .into(imageView);
    }

    public void display(File file, @DrawableRes int holderResId, @DrawableRes int errorResId, ImageView imageView) {
        Glide.with(mContext)
            .load(file)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderResId)
            .error(errorResId)
            .into(imageView);
    }

    public void display(File file, @DrawableRes int holderResId, ImageView imageView) {
        Glide.with(mContext)
            .load(file)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .placeholder(holderResId)
            .into(imageView);
    }

    public void display(String url, ImageView imageView) {
        Glide.with(mContext)
            .load(url)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .into(imageView);
    }

    public void fetchImage(String url, int width, int height) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mContext).load(url).downloadOnly(width, height);
        }
    }
}
