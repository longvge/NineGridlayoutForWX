package com.lukey.ninegridlayout;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/1.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context applicationContext) {
        // TODO Auto-generated method stub

            File cacheDir = StorageUtils.getOwnCacheDirectory(applicationContext, "app/cache");

            // DisplayImageOptions defaultOptions = new
            // DisplayImageOptions.Builder()
            // .cacheInMemory(true)
            // .cacheOnDisc(true)
            // .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    applicationContext)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    // .writeDebugLogs() // Remove for release app
                    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                    // .discCache(new UnlimitedDiskCache(cacheDir)) // default
                    // .defaultDisplayImageOptions(defaultOptions)
                    .diskCacheSize(10 * 1024 * 1024)
                    // .discCacheSize(10 * 1024 * 1024)
                    .build();

            // Initialize ImageLoader with configuration.

            if (!ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().init(config);
            }

    }
}
