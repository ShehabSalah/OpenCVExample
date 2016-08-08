package com.example.opencv.opencvexample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //load openCV libs
    //Important for running the openCV libs
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.i(LOG_TAG, "OpenCV initialization field");
        } else {
            Log.i(LOG_TAG, "OpenCV initialization succeeded");
        }
    }

    ImageView imageView;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);

        imageView = (ImageView)findViewById(R.id.image);

        //Convert image from Image vieew to bitmap image
        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //Button must be not equal null
        assert button != null;
        //OnClick  Listener
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                new ConvertInBackground().execute();
            }
        });
    }
    /* *
     * This class used to convert the image to black and white using OpenCV in the background Thread
     * if you try to create mat variable or make operation in the main thread using openCV for
     * example in onCreate, the application will crash
     * Read about AsyncTask: https://developer.android.com/reference/android/os/AsyncTask.html
     * */
    public class ConvertInBackground extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //Create new OpenCV variable with the same width and height of the bitmap variable
            Mat mat = new Mat (bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8U);

            //Convert the Bitmap to Mat
            Utils.bitmapToMat(bitmap, mat);

            //Convert image to gray scale
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);

            //Return the image from mat to Bitmap after convert it to gray scale using OpenCV libs
            Utils.matToBitmap(mat, bitmap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //Update the imageView with the new gray scale image
            imageView.setImageBitmap(bitmap);

        }
    }
}
