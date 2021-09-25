package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phoneNum;
    Button call;
    EditText latitude;
    EditText longitude;
    Button viewMap;
    Button imageCapture;
    Button viewContacts;
    Button saveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setXML();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callButtonClicked();
            }
        });


        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewMapButtonClicked();
            }
        });

        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThumbnailCapture.class);
                startActivity(intent);
            }
        });

        viewContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ViewContacts.class);
                startActivity(intent);
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SaveImage.class);
                startActivity(intent);
            }
        });

    }

    private void setXML()
    {
        phoneNum = findViewById(R.id.editTextPhone);
        call = findViewById(R.id.callButton);
        latitude = findViewById(R.id.editTextLat);
        longitude = findViewById(R.id.editTextLong);
        viewMap = findViewById(R.id.viewMap);
        imageCapture = findViewById(R.id.imageCapture);
        viewContacts = findViewById(R.id.viewContacts);
        saveImage = findViewById(R.id.saveImage);
    }

    private void callButtonClicked(){
        if(phoneNum.length()==10)
        {
            int phone = Integer.parseInt(phoneNum.getText().toString());
            Uri uri = Uri.parse(String.format("tel:%d", phone));
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(uri);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Invalid Phone number",Toast.LENGTH_SHORT).show();
        }
    }

    private void viewMapButtonClicked(){
        if(longitude.length()<=0 || latitude.length()<=0)
        {
            Toast.makeText(this,"Invalid Cordinates",Toast.LENGTH_SHORT).show();
        }
        else {
            double latitude = Double.parseDouble(this.latitude.getText().toString());
            double longitute = Double.parseDouble(this.longitude.getText().toString());
            Uri uri = Uri.parse(String.format("geo:%f,%f", latitude, longitute));
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }
}