package yc.com.cameraapi1sdk;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.lang.reflect.Method;
import java.util.Date;

public class Camera1Fragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Camera mCamera;
    private CameraPreview mPreview;
    private int liveFrameDelay = 0;
    private Date liveFrameTimer;
    private boolean isLiveFrameTaskRunning = false;

    private FrameLayout fragmentLayout;
    private Button takePicBtn;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-26 19:33:41 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        fragmentLayout = (FrameLayout) view.findViewById(R.id.fragmentLayout);
        takePicBtn = (Button) view.findViewById(R.id.takePicBtn);
    }


    public Camera1Fragment() {
        // Required empty public constructor
    }

    void init() {

        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null)
                    mCamera.takePicture(null, null, mPicture);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera1, container, false);
        findViews(view);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        // Create an instance of Camera
        mCamera = getCameraInstance();

        if (mCamera != null) {

            setDisplayOrientation(mCamera, 90);

            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getActivity(), mCamera);
            mPreview.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {

                    if (!isLiveFrameTaskRunning && (liveFrameDelay == 0 || liveFrameTimer == null || new Date().getTime()-liveFrameTimer.getTime() >= liveFrameDelay))
                    {
                        isLiveFrameTaskRunning = true;
                        if (onPreviewFrameListener != null)
                            onPreviewFrameListener.onPreviewFrame(data,camera);
                        liveFrameTimer = new Date();
                        isLiveFrameTaskRunning = false;
                    }

                }
            });
            FrameLayout preview = (FrameLayout) view.findViewById(R.id.fragmentLayout);
            preview.addView(mPreview);
        }

        init();

        return view;
    }



    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.d("yc","camera exception->"+e.toString());
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    protected void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[]{angle});
        } catch (Exception e1) {
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if (onPictureTakenListener != null)
                onPictureTakenListener.onPictureTaken(data,camera);
        }
    };

    public interface onPictureTakenListener
    {
        void onPictureTaken(byte[] data, Camera camera);
    }
    onPictureTakenListener onPictureTakenListener;

    public void setOnPictureTakenListener(onPictureTakenListener onPictureTakenListener)
    {
        this.onPictureTakenListener = onPictureTakenListener;
    }

    public interface onPreviewFrameListener
    {
        void onPreviewFrame(byte[] data, Camera camera);
    }
    onPreviewFrameListener onPreviewFrameListener;

    public void setOnPreviewFrameListener(onPreviewFrameListener onPreviewFrameListener)
    {
        liveFrameDelay = 0;
        this.onPreviewFrameListener = onPreviewFrameListener;
    }

    public void setTimeDelayOnPreviewFrameListener(int delay, onPreviewFrameListener onPreviewFrameListener)
    {
        liveFrameDelay = delay;
        this.onPreviewFrameListener = onPreviewFrameListener;
    }

}
