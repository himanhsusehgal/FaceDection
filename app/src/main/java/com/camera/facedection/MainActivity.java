package com.camera.facedection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button frontCamera,backCamera;

    // camera permission requestCode
    int requestCodeCamera = 500;

    BasicFeatures basicFeatures;

    LinkedHashSet<String> frontCameraResolution;
    LinkedHashSet<String> backCameraResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frontCameraResolution = new LinkedHashSet<>();
        backCameraResolution = new LinkedHashSet<>();

        frontCamera = findViewById(R.id.frontCamera);
        backCamera = findViewById(R.id.backCamera);

        basicFeatures = new BasicFeatures(MainActivity.this);

        checkCameraPermission();

        frontCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] resolutions= new String[frontCameraResolution.size()];
                int temp = 0;
                for(String resolution : frontCameraResolution){
                    resolutions[temp] = resolution;
                    temp++;
                }

                basicFeatures.alertDialogList("Resolutions",resolutions,false);
            }
        });

        backCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] resolutions= new String[backCameraResolution.size()];
                int temp = 0;
                for(String resolution : backCameraResolution){
                    resolutions[temp] = resolution;
                    temp++;
                }

                basicFeatures.alertDialogList("Resolutions",resolutions,true);
            }
        });


    }


    public void checkCameraPermission(){

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkCameraPermission: Request For Camera Permission ");
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, requestCodeCamera);
        }else {
            Log.d(TAG, "checkCameraPermission: Already have Camera Permission ");
            getCameraDetails();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == requestCodeCamera){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: After Camera Permission ");
                getCameraDetails();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},requestCodeCamera);
            }
        }
    }


    public void getCameraDetails(){

        int numOfCamera = Camera.getNumberOfCameras();


        for(int i = 0 ; i < numOfCamera ; i++){

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i,cameraInfo);


            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                Camera camera = Camera.open(i);
                Camera.Parameters parameters = camera.getParameters();
                for (int j = 0;j < parameters.getSupportedPictureSizes().size();j++)
                {
                    frontCameraResolution.add(parameters.getSupportedPictureSizes().get(j).width + "   x   "+ parameters.getSupportedPictureSizes().get(j).height);

                }

                camera.release();
            }
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){

                Log.d(TAG, "getCameraDetails: ____________________________________________________________________");
                Camera cameraface = Camera.open(i);

                Camera.Parameters parameters = cameraface.getParameters();

                for (int j = 0;j < parameters.getSupportedPictureSizes().size();j++)
                {

                    backCameraResolution.add(parameters.getSupportedPictureSizes().get(j).width + "   x   "+ parameters.getSupportedPictureSizes().get(j).height);


                }

                cameraface.release();
            }


        }


    }
}
