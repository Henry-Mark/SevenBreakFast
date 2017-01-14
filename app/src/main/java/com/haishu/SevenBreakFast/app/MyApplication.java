package com.haishu.SevenBreakFast.app;

import android.app.Application;

import com.haishu.SevenBreakFast.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by zyw on 2016/3/3.
 */
public class MyApplication extends Application {
    public static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        imageLoader = ImageLoader.getInstance();
        loadPictureSettings();
    }

    /**
     * 加载图片设置
     */
    private void loadPictureSettings() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.icon_stub)//设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty)//设置图片Url为空或者错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error)//设置图片加载或者解码过程发生错误显示图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在sd卡中
                .build();//创建配置过的DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        imageLoader.init(config);
    }
}
