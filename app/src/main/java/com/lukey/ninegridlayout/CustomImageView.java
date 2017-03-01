package com.lukey.ninegridlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

/**
 * creator  Lukey on 2016/6/14
 */
public class CustomImageView extends ImageView {
    private String url;
    private ImageLoader imageLoader;
    private DisplayImageOptions itemImgOptions;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions.Builder builder = new  DisplayImageOptions.Builder();
        itemImgOptions = builder
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true)
//        .preProcessor(new BitmapProcessor() {          //这里是指压缩图片不
//            @Override
//            public Bitmap process(Bitmap bitmap) {
//                return BitmapUtil.compressImage(bitmap, 50);
//            }
//        })
                .cacheOnDisk(true).build();// .cacheOnDisc(true)
    }

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    public void setImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            imageLoader.displayImage(url,this,itemImgOptions);
        }
    }
}
