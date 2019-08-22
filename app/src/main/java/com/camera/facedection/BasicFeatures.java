package com.camera.facedection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.camera.facedection.Activity.CameraActivity;

public class BasicFeatures {

    Context context;


    public BasicFeatures(Context context) {
        this.context = context;

    }


    public void alertDialogList(String title, final String[] list, final Boolean backCamera){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                context.startActivity(new Intent(context.getApplicationContext(), CameraActivity.class)
                                     .putExtra("backCamera",backCamera)
                                     .putExtra("resolution",list[i]));

            }
        });
        builder.show();

    }
}
