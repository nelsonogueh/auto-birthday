package com.dulceprime.specialwishes.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.SQLController;
import com.dulceprime.specialwishes.other_components.SomeComponents;

import java.util.ArrayList;
import java.util.List;

public class Birthday extends AppCompatActivity {

    private ListView messageLV;
    List<String> messagesList;
    SQLController sqlController;
    SQLiteDatabase db;


    List<String> messagesIdDB;
    List<String> messagesBodyDB;
    List<String> messageContactDB;
    List<String> birthDayDB;
    List<String> birthdMonthDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        sqlController = new SQLController(this);

        // FETCHING THE DATA FROM DB
        fetchScheduledBirthdayMessages();


        messageLV = (ListView) findViewById(R.id.scheduledBirthdayLV);
        MyMessageAdapter messageAdapter = new MyMessageAdapter(getApplicationContext(), messagesBodyDB, messageContactDB, birthDayDB, birthdMonthDB);
        messageLV.setAdapter(messageAdapter);

    }

    public void fetchScheduledBirthdayMessages() {

        messagesIdDB = new ArrayList<String>();
        messagesBodyDB = new ArrayList<String>();
        messageContactDB = new ArrayList<String>();
        birthDayDB = new ArrayList<String>();
        birthdMonthDB = new ArrayList<String>();


        // TODO: Get all the message from SQLite and set them to the List

        DBhelper dBhelper = new DBhelper(this);
        db = dBhelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBhelper.MESSAGE_SENDING_TABLE + " ORDER BY " + DBhelper.MESSAGE_SENDING_ID + " DESC", null);

        while (c.moveToNext()) {
            // Populating the List

            messagesIdDB.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_ID)));
            messagesBodyDB.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_BODY)));
            messageContactDB.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_RECIPENT)));
            birthDayDB.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_DAY)));
            birthdMonthDB.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_MONTH)));
        }

        c.close();
        db.close();
    }

    class MyMessageAdapter extends ArrayAdapter {

        List<String> messagesBody;
        List<String> messageContact;
        List<String> birthDay;
        List<String> birthdMonth;

        SomeComponents myComponent;

        private MyMessageAdapter(Context context, List<String> messagesBody, List<String> contactPhoneNumber, List<String> birthday, List<String> birthMonth) {
            //Overriding Default Constructor off ArratAdapter
            super(context, R.layout.activity_birthday_row, messagesBody);

            this.messagesBody = messagesBody;
            this.messageContact = contactPhoneNumber;
            this.birthDay = birthday;
            this.birthdMonth = birthMonth;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Inflating the layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.activity_birthday_row, parent, false);

            myComponent = new SomeComponents();

            //Get the reference to the view objects
            TextView messageBodyTV = (TextView) row.findViewById(R.id.birthday_messagebodyTV);
            TextView BirthdayContactTV = (TextView) row.findViewById(R.id.birthdayContactTV);
            TextView birthDateTV = (TextView) row.findViewById(R.id.birthdateTV);


            //Providing the element of an array by specifying its position
            messageBodyTV.setText(messagesBody.get(position));
            BirthdayContactTV.setText(myComponent.getContactName(getApplicationContext(), myComponent.removeSpaceAndHyphen(messageContact.get(position))));
            birthDateTV.setText(SomeComponents.dateDaySuffix(birthDay.get(position)) + " " + SomeComponents.dateMonthName(birthdMonth.get(position)));
            return row;
        }
    }
}
