package com.example.myapplication;

import android.Manifest;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;

public class ViewContacts extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;
    Button pickContact;
    Button more;
    TextView email;
    TextView emailLabel;
    TextView phone;
    TextView phoneLabel;
    TextView name;
    TextView nameLabel;
    TextView id;
    TextView idLabel;
    String emailText = "";
    String phoneText = "";

    int contactId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        pickContact = findViewById(R.id.pickContact);
        email = findViewById(R.id.emailView);
        emailLabel = findViewById(R.id.textViewEmail);
        phone = findViewById(R.id.phoneView);
        phoneLabel = findViewById(R.id.textViewPhone);
        name = findViewById(R.id.nameView);
        nameLabel = findViewById(R.id.textViewName);
        id = findViewById(R.id.idView);
        idLabel = findViewById(R.id.textViewID);
        more = findViewById(R.id.moreButton);

        id.setVisibility(View.INVISIBLE);
        idLabel.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        nameLabel.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        phoneLabel.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        emailLabel.setVisibility(View.INVISIBLE);
        more.setVisibility(View.INVISIBLE);

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContactButtonClicked();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ViewContacts.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ViewContacts.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);
                }
                else {
                    moreButtonClicked();
                }
            }
        });
    }

    private void pickContactButtonClicked(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,REQUEST_CONTACT);
        phone.setVisibility(View.INVISIBLE);
        phoneLabel.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        emailLabel.setVisibility(View.INVISIBLE);
    }

    private void moreButtonClicked(){

        emailText = "";
        phoneText = "";
        getEmails();
        getPhoneNums();
        email.setText(emailText);
        phone.setText(phoneText);
        emailLabel.setVisibility(View.VISIBLE);
        phoneLabel.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
    }

    private void getEmails()
    {
        Uri emailUri = Email.CONTENT_URI;
        String[] queryFields = new String[] {
                Email.ADDRESS,
                Email.TYPE
        };

        String whereClause = Email.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor c = getContentResolver().query(
                emailUri, queryFields, whereClause,whereValues, null);
        try{
            c.moveToFirst();
            do{
                String emailAddress = c.getString(0);
                int type = c.getInt(1);
                String strType = "";
                if(type == (Email.TYPE_HOME))
                {
                    strType = "HOME";
                }
                else if (type == (Email.TYPE_WORK))
                {
                    strType = "WORK";
                }
                else
                {
                    strType = "OTHER";
                }
                String line = strType + " :" + emailAddress;
                emailText = line+"\n"+emailText;
            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }
    }

    private void getPhoneNums()
    {
        Uri emailUri = Phone.CONTENT_URI;
        String[] queryFields = new String[] {
                Phone.NUMBER,
                Phone.TYPE
        };

        String whereClause = Phone.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor c = getContentResolver().query(
                emailUri, queryFields, whereClause,whereValues, null);
        try{
            c.moveToFirst();
            do{
                String phoneNum = c.getString(0);
                int type = c.getInt(1);
                String strType = "";
                if(type == (Phone.TYPE_HOME))
                {
                    strType = "HOME";
                }
                else if (type == (Phone.TYPE_WORK))
                {
                    strType = "WORK";
                }
                else if (type == (Phone.TYPE_MOBILE))
                {
                    strType = "MOBILE";
                }
                else
                {
                    strType = "OTHER";
                }
                String line = strType + " :" + phoneNum;
                phoneText = line +"\n"+phoneText;
            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CONTACT && resultCode == RESULT_OK){
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getContentResolver().query(
                    contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    this.contactId = c.getInt(0);         // ID first
                    String contactName = c.getString(1); // Name second
                    name.setVisibility(View.VISIBLE);
                    nameLabel.setVisibility(View.VISIBLE);
                    name.setText(contactName);
                    id.setVisibility(View.VISIBLE);
                    idLabel.setVisibility(View.VISIBLE);
                    id.setText(Integer.toString(this.contactId));
                    more.setVisibility(View.VISIBLE);
                }
            }
            finally {
                c.close();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_READ_CONTACT_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contact Reading Permission Granted", Toast.LENGTH_SHORT).show();
                moreButtonClicked();
            }
        }
    }
}