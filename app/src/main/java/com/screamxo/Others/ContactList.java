package com.screamxo.Others;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactList {

    private Context context;
    private String[] selectionArgs = {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
    };

    public ContactList(Context context) {
        this.context = context;
    }

    public String getContacts() {
        StringBuilder emailJson = new StringBuilder();
        emailJson.append("[");
        ContentResolver resolver = context.getContentResolver();
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC";
        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)";
        Cursor c = resolver.query(ContactsContract.Data.CONTENT_URI, null, selection, selectionArgs,
                sortOrder);
        try {
            assert c != null;
            while (c.moveToNext()) {

                String data1 = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                if (data1.contains("@")) {
                    emailJson.append("{\"email_id\":\"").append(data1).append("\"},");
                }
            }
            if (emailJson.length() > 2) {
                emailJson.deleteCharAt(emailJson.length() - 1);
            }
            emailJson.append("]");
            Log.v("data1:", " " + emailJson.toString());
            return emailJson.toString();
        } finally {
            assert c != null;
            c.close();
        }

    }
}





