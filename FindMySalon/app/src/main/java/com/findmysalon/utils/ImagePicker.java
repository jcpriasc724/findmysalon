/*
package com.findmysalon.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.findmysalon.R;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ImagePicker {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ADD PHOTO");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo"))
            {
                // Check if camera is supported in user's device
                if(isDeviceSupportCamera()){
                    takePhoto(context);
                }
                else{// Display message if camera is not supported
                    //Toast.makeText(getActivity().getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
            else if (options[item].equals("Choose from Gallery"))
            {
                chooseFromGallery(context);
            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void chooseFromGallery(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(context, intent, REQUEST_GALLERY, null);
    }

    private void takePhoto(Context context) {
        // Intent to open camera
        Intent takePictureIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Checking if there any app that can handle the IMAGE_CAPTURE intent to avoid app crash
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE){
                */
/*Bitmap imageBitmap = BitmapFactory.decodeFile(pathToFile);
                Log.d("Path to file", ""+pathToFile);*//*

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImgView.setImageBitmap(imageBitmap);
            }
            else if(requestCode == REQUEST_GALLERY){
                //Get selected image uri here
                Uri selectedImage = data.getData();
                mImgView.setImageURI(selectedImage);
            }

        }
    }
}
*/
