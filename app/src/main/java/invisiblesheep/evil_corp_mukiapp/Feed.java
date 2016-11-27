package invisiblesheep.evil_corp_mukiapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by tuomas on 26.11.2016.
 */

public class Feed {
    private ArrayList<String> feedpics;
    private String feedname;
    private String id;

    public Feed(String feedname, String id, ArrayList<String> feedpics){
        this.feedpics = feedpics;
        this.feedname = feedname;
        this.id = id;
    }


}
