package invisiblesheep.evil_corp_mukiapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.muki.core.MukiCupApi;
import com.muki.core.MukiCupCallback;
import com.muki.core.model.Action;
import com.muki.core.model.DeviceInfo;
import com.muki.core.model.ErrorCode;
import com.muki.core.model.ImageProperties;
import com.muki.core.util.ImageUtils;

import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private EditText mSerialNumberEdit;
    private MukiCupApi mMukiCupApi;
    private TextView textView2;
    private Bitmap mImage;
    private int mContrast = ImageProperties.DEFAULT_CONTRACT;
    private static ImageView mCupImage;
    private String mCupId;
    public static final String TAG = AppController.class.getSimpleName();
    public Random random;

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSerialNumberEdit = (EditText) findViewById(R.id.editText);
        addListenerOnButton1((Button) findViewById(R.id.button));
        textView2 = (TextView) findViewById(R.id.textView2);
        mCupImage = (ImageView) findViewById(R.id.imageView);

        random = new Random();

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
        mImage = ImageUtils.scaleBitmapToCupSize(image);
        mContrast = ImageProperties.DEFAULT_CONTRACT;
        mMukiCupApi = new MukiCupApi(getApplicationContext(), new MukiCupCallback() {
            @Override
            public void onCupConnected() {
                showToast("Cup connected");
            }

            @Override
            public void onCupDisconnected() {
                showToast("Cup disconnected");
            }

            @Override
            public void onDeviceInfo(DeviceInfo deviceInfo) {
                textView2.setText(deviceInfo.toString());
            }

            @Override
            public void onImageCleared() {
                showToast("Image cleared");
            }

            @Override
            public void onImageSent() {
                showToast("Image sent");
            }

            @Override
            public void onError(Action action, ErrorCode errorCode) {
                showToast("Error:" + errorCode + " on action:" + action);
            }
        });

    }

    public static void setImageviewBitmap(Bitmap bitmap){
        mCupImage.setImageBitmap(bitmap);
    }

    public void addListenerOnButton1(final Button button){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String id = mSerialNumberEdit.getText().toString();
                ServerConnection.makeArrayRequest(id);
            }
        });

    }

    private void showToast(final String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void setupImage() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap result = Bitmap.createBitmap(mImage);
                ImageUtils.convertImageToCupImage(result, mContrast);
                return result;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mCupImage.setImageBitmap(bitmap);
            }
        }.execute();
    }

    public void request(View view) {
        Log.d(TAG, "request: In request");
        String serialNumber = mSerialNumberEdit.getText().toString();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String serialNumber = strings[0];
                    return MukiCupApi.cupIdentifierFromSerialNumber(serialNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                mCupId = s;
                textView2.setText(s);
            }
        }.execute(serialNumber);
    }

    public void send(View view) {
//        Log.d(TAG, "send: send image to muki");
        final ArrayList<Bitmap> feedpics = AppController.getInstance().getFeedpics();
        Handler handler1 = new Handler();
//        ImageUtils.convertImageToCupImage(feedpics.get(0), mContrast);
//        mMukiCupApi.sendImage(feedpics.get(0), new ImageProperties(mContrast), mCupId);

        for(int i = 1; i < feedpics.size(); i++){
            handler1.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Log.d(TAG, "run: send image to muki");
                    Bitmap bitmap = feedpics.get(random.nextInt(feedpics.size()));
                    Bitmap mBitmap = ImageUtils.scaleBitmapToCupSize(bitmap);
                    ImageUtils.convertImageToCupImage(mBitmap, mContrast);
                    mCupImage.setImageBitmap(mBitmap);
                    mMukiCupApi.sendImage(mBitmap, new ImageProperties(mContrast), mCupId);

                }
            }, 10000 * i);
        }

        //setupImage();
//        Bitmap bitmap = feedpics.get(0);
//        Bitmap mBitmap = ImageUtils.cropImage(bitmap, new Point(100, 0));
//        ImageUtils.convertImageToCupImage(mBitmap, mContrast);
//        Log.d(TAG, "send: Sending bitmap to muki");
//        mCupImage.setImageBitmap(mBitmap);
//        mMukiCupApi.sendImage(mBitmap, new ImageProperties(mContrast), mCupId);
    }

}
