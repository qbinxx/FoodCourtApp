package com.joker.foodcourtapp.utils;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;


public class AppController extends Application {

	public static final String TAG = AppController.class
			.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;
	public static volatile Handler applicationHandler = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		CookieManager manager = new CookieManager();
		CookieHandler.setDefault(manager);

		applicationHandler = new Handler(getInstance().getMainLooper());
		//NativeLoader.initNativeLibs(AppController.getInstance());
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	// Adding request to request queue
//	AppController.getInstance().addToRequestQueue(jsonObjReq,
//												  tag_json_obj);
}
