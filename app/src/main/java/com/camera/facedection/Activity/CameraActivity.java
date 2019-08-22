package com.camera.facedection.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.camera.facedection.R;
import com.camera.facedection.Utils.CameraPreview;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    LinearLayout cameraPreview;
    public Camera mCamera;
    private CameraPreview mPreview;
    Boolean backCamera;
    CameraCharacteristics cameraCharacteristics;


    int width,height;
    String[] res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraPreview = findViewById(R.id.cPreview);


        Intent intent = getIntent();
        backCamera = intent.getBooleanExtra("backCamera",true);
        int fb=1;
        if(backCamera){
            fb = 0;
        }else {
            fb = 1;
        }


        res = intent.getStringExtra("resolution").split("x");

        width = Integer.parseInt(res[0].trim());
        height = Integer.parseInt(res[1].trim());

        mCamera =  Camera.open(fb);
        mCamera.setDisplayOrientation(90);
        mPreview = new CameraPreview(CameraActivity.this, mCamera);


        cameraPreview.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);

            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        }else {
            Log.d("nu","no null");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseCamera();
        cameraPreview.removeAllViews();
    }

    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}
