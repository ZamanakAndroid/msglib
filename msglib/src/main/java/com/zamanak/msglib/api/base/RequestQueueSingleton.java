package com.zamanak.msglib.api.base;

/**
 * Created by PirFazel on 11/21/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    private static RequestQueueSingleton _instance;
    private RequestQueue _requestQueue;
    private ImageLoader _imageLoader;
    private static Context _context;

    private RequestQueueSingleton(Context context) {
        _context = context;
        _requestQueue = getRequestQueue();

        _imageLoader = new ImageLoader(_requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (_instance == null) {
            _instance = new RequestQueueSingleton(context);
        }
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (_requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            _requestQueue = Volley.newRequestQueue(_context.getApplicationContext());
        }
        return _requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return _imageLoader;
    }
}