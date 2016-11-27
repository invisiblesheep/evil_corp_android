package invisiblesheep.evil_corp_mukiapp;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.android.volley.*;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageRequest;

import java.util.*;

import static android.content.ContentValues.TAG;

import com.android.volley.toolbox.JsonObjectRequest;
//import com.mongodb.*;

/**
 * Created by tuomas on 26.11.2016.
 */

public class ServerConnection {

    public static final String TAG = AppController.class.getSimpleName();
    public static final String kaffeedurl = "http://kaffeed.com/";


    public static ArrayList<Feed> buildFeedFromJson(JSONArray array){
        ArrayList<Feed> feed = new ArrayList<>();

        try{
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("feed_id");
                String name = object.getString("name");
                JSONArray pics = object.getJSONArray("pics");
                ArrayList<String> picsArray = new ArrayList<>();
                for(int j = 0; j < pics.length(); j++){
                    JSONObject pic = pics.getJSONObject(j);
                    picsArray.add(pic.getString("url"));
                }
                Log.d(TAG, "buildFeedFromJson: pics: " + pics);
//                String pic1 = object.getJSONArray("pics").getString(0);
//                pic1 = pic1.substring(8, pic1.length() - 2);
//                String pic2 = object.getJSONArray("pics").getString(1);
//                pic2 = pic2.substring(8, pic2.length() - 2);
//                String pic3 = object.getJSONArray("pics").getString(2);
//                pic3 = pic3.substring(8, pic3.length() - 2);
//                ArrayList<String> picIds = new ArrayList<>();
//                picIds.add(pic1);
//                picIds.add(pic2);
//                picIds.add(pic3);
                AppController.getInstance().clearBitmapCache();
                for(String url : picsArray){
                    Log.d(TAG, "buildFeedFromJson: url: " + url);
                    loadBitmapFromUrl(url);
                }



            }

        }catch (JSONException e){
            System.out.println("Exception" + e.getMessage());
        }

        return feed;
    }

    public static void loadBitmapFromUrl(String url){
        ImageRequest request = new ImageRequest(kaffeedurl + "uploads/" + url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        AppController.getInstance().saveBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                });
// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

//    public static void loadImageFromUrl(String url){
//        ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
//        mImageLoader.get(url, )
//    }
    public static void makeArrayRequest(String id){

        String tag_json_array = "json_array_req";

        String url = kaffeedurl + "api/feeds/" + id;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        ServerConnection.buildFeedFromJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Log.d(TAG, "makeArrayRequest: " + AppController.getInstance().getTest());
        AppController.getInstance().addToRequestQueue(req, tag_json_array);
    }


    /*
    public static void updateUserFeeds(){
        try{
            Log.d(TAG, "updateUserLocation...");
            //Flag flag = AppController.getInstance().getActivePlayerFlag();
            final JSONObject jsonBody = new JSONObject(flag.toJsonString());

            String tag_json_obj = "json_obj_req";

            String putUrl = koffeedurl + "/" + flag.getId();

            Log.d(TAG, putUrl);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, putUrl, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            AppController.getInstance().setActivePlayerFlag(ServerConnection.buildFlagFromJson(response));
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error){
                    error.printStackTrace();
                }
            });

            if(AppController.getInstance() == null){
                Log.d(TAG, "AppController instance null!");
            } else {
                AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    */

    public static void getFeedsFromServer(){
        Log.d(TAG, "getLeaguesFromServer...");

        String tag_json_array = "json_array_req";

        JsonArrayRequest req = new JsonArrayRequest(kaffeedurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //ServerConnection.buildLeaguesFromResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(req, tag_json_array);

    }
}
