package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.os.BuildCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage extends AppCompatActivity {

    private static final int REQUEST_PHOTO = 3;
    File photoFile;
    Button takePhotoButton;
    ImageView photoView;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        photoView = findViewById(R.id.photoView);
        save = findViewById(R.id.save);
        save.setVisibility(View.INVISIBLE);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoButtonClicked();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorMatrix matrix = new ColorMatrix(); //https://developer.android.com/reference/android/graphics/ColorMatrix#setSaturation(float)
                matrix.setSaturation(0);
                photoView.setColorFilter(new ColorMatrixColorFilter(matrix));
            }
        });
    }

    private void photoButtonClicked(){

        photoFile = new File(getFilesDir(),"photo.jpg");
        Uri cameraUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID+".fileprovider",photoFile);
        Intent photoIntent = new Intent();
        photoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,cameraUri);

        PackageManager pm = getPackageManager();
        for(ResolveInfo a : pm.queryIntentActivities(
                photoIntent, PackageManager.MATCH_DEFAULT_ONLY)) {

            grantUriPermission(a.activityInfo.packageName, cameraUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        startActivityForResult(photoIntent, REQUEST_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_PHOTO && resultCode == RESULT_OK){
            Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
            photoView.setImageBitmap(photo);
            save.setVisibility(View.VISIBLE);
        }
    }
}