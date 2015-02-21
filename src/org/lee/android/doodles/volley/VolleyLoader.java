package org.lee.android.doodles.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

import java.io.File;


public class VolleyLoader {
    private static VolleyLoader mInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private BitmapCache mBitmapCache;

    private VolleyLoader(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mBitmapCache = new BitmapCache();
        mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache);
    }

    public static void initialize(Context context) {
        mInstance = new VolleyLoader(context);
    }

    public static VolleyLoader getInstance() {
        return mInstance;
    }

    public Bitmap getBitmap(String url) {
        return mBitmapCache.getBitmap(url);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void cancelAll() {
        mRequestQueue.cancelAll(new RequestFilter() {
            @Override
            public boolean apply(Request<?> arg0) {
                return true;
            }
        });
    }

    public boolean clearCache(Context context) {
        if(context == null) return false;
        String volleyCacheDir = context.getCacheDir().getAbsolutePath() + File.separator + "volley";
        return FileUtils.clear(volleyCacheDir);
    }

    public class BitmapCache implements ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
//			int mCacheSize = maxMemory / 8;
            int mCacheSize = 20;
            mCache = new LruCache<String, Bitmap>(mCacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
