package yc.com.cameraapi1demo;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import yc.com.cameraapi1sdk.Camera1;
import yc.com.cameraapi1sdk.Camera1Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Camera1.init(this);
        Camera1.getCamera1Fragment().setOnPictureTakenListener(new Camera1Fragment.onPictureTakenListener() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("yc","image taken");
            }
        });
//        Camera1.getCamera1Fragment().setOnPreviewFrameListener(new Camera1Fragment.onPreviewFrameListener() {
//            @Override
//            public void onPreviewFrame(byte[] data, Camera camera) {
//                Log.d("yc","live frame");
//            }
//        });
        Camera1.getCamera1Fragment().setTimeDelayOnPreviewFrameListener(500, new Camera1Fragment.onPreviewFrameListener() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Log.d("yc","task");

                for (int i=0; i< 100000000; i++){}

                Log.d("yc","task done");


            }
        });
    }
}
