package net.appifiedtech.marshmallowchanges;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import net.appifiedtech.adapters.ContactAdapter;
import net.appifiedtech.model.Contact;

import java.util.ArrayList;

public class RunTimePermissionActivity extends AppCompatActivity {

    private ListView listView;
    private ContactAdapter adapter;
    private static final int CODE_CONTACT_READ = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_time_permission);
        listView = (ListView) findViewById(R.id.listViewContacts);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(RunTimePermissionActivity.this, Manifest.permission.READ_CONTACTS)  == PackageManager.PERMISSION_GRANTED)
            {
                // Permission Already Given, do ur code
                showContactList();
            }
            else
            {
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS))
                {
                    Toast.makeText(RunTimePermissionActivity.this, "This Permission is neaded to show contact list", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},CODE_CONTACT_READ);
            }
        }
        else
        {
            // No need of Permission , Its pre-marshmallow
            showContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == CODE_CONTACT_READ)
        {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // permission was granted, do ur code
                showContactList();
            }
            else
            {
                Toast.makeText(RunTimePermissionActivity.this, "Sorry you have denied the permission to view contact", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showContactList() {
        ArrayList<Contact> contactsList = new ArrayList<Contact>();
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                contact.setId(contact_id);
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                contact.setName(name);
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    StringBuffer phNumber = new StringBuffer("");
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phNumber.append("," + phoneNumber);
                    }
                    contact.setPhone(phNumber.toString());
                    phoneCursor.close();
                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
                    StringBuffer emailIdd = new StringBuffer("");
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        emailIdd.append("," + email);
                    }
                    contact.setEmail(emailIdd.toString());
                    emailCursor.close();
                }
                contactsList.add(contact);
            }
        }

        if(contactsList!=null){
            adapter = new ContactAdapter(RunTimePermissionActivity.this,contactsList);
            listView.setAdapter(adapter);
        }
    }
}
