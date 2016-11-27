package invisiblesheep.evil_corp_mukiapp;

/**
 * Created by tuomas on 26.11.2016.
 */

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AppController extends Application{
    public static final String TAG = AppController.class.getSimpleName();

    private String test = "test";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruBitmapCache lruBitmapCache;

    private ArrayList<Bitmap> feedpics;

    //private boolean isUpdatingLocation;

    //private Flag activePlayerFlag;

    //private String activeLogo;

    //private ArrayList<League> leagues;

    private static AppController mInstance;

    public String getTest(){
        return this.test;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        lruBitmapCache = new LruBitmapCache();
        feedpics = new ArrayList<>();
    }

    public static synchronized AppController getInstance(){
        return mInstance;
    }

//    public void setIsUpdatingLocation(boolean b){
//        isUpdatingLocation = b;
//    }
/*
    public void setActivePlayerFlag(Flag flag){ activePlayerFlag = flag;}
    public Flag getActivePlayerFlag(){ return activePlayerFlag;}

    public void setActiveLogo(String s){
        activeLogo = s;
    }

    public String getActiveLogo(){
        return activeLogo;
    }

    public boolean getIsUpdatingLocation(){
        return isUpdatingLocation;
    }

    public void setLeagues(ArrayList<League> leagues){
        this.leagues = leagues;
    }

    public ArrayList<League> getLeagues(){
        return leagues;
    }
    */

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ArrayList<Bitmap> getFeedpics(){
        return this.feedpics;
    }

//    public static void cacheBitmap(String url, Bitmap bitmap){
//
//    }

    public void saveBitmap(Bitmap bitmap){
        feedpics.add(bitmap);
        MainActivity.setImageviewBitmap(bitmap);
        Log.d(TAG, "saveBitmap: " + bitmap);
    }

    public void clearBitmapCache(){
        feedpics = new ArrayList<>();
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if(mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
