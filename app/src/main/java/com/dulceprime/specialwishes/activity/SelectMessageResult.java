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

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.other_components.DBhelper;

import java.util.ArrayList;
import java.util.List;

public class SelectMessageResult extends AppCompatActivity {

    private ListView messageLV;
    List<String> messagesList;
    String requestType = null;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_message_result);

        try {
            // Getting what type of request was made
            requestType = getIntent().getStringExtra("RequestType");

            switch (requestType) {
                case "SELECT_MESSAGE": {
                    fetchDBMessage();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Request: ", "No request type passed!!!");
        }


    }

    public void fetchDBMessage() {
        messagesList = new ArrayList<String>();
        // Populating the List

        DBhelper dBhelper = new DBhelper(this);
        db = dBhelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBhelper.BIRTHDAY_MESSAGES_TABLE + " ORDER BY " + DBhelper.MESSAGE_ID + " DESC", null);

        while (c.moveToNext()) {
            // Populating the List
            messagesList.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_BODY)));
        }

        c.close();
        db.close();


        messageLV = (ListView) findViewById(R.id.messageLV);
        MyMessageAdapter messageAdapter = new MyMessageAdapter(getApplicationContext(), messagesList);
        messageLV.setAdapter(messageAdapter);
        messageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Returning the result message to the activity that requests it.
                Intent intent = new Intent();
                Bundle resultBundle = new Bundle();
                resultBundle.putString("resultMessage", messagesList.get(i));
                intent.putExtras(resultBundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    class MyMessageAdapter extends ArrayAdapter {

        List<String> messages;

        public MyMessageAdapter(Context context, List<String> messages) {
            //Overriding Default Constructor off ArratAdapter
            super(context, R.layout.message_single_row, messages);

            this.messages = messages;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Inflating the layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.message_single_row, parent, false);

            //Get the reference to the view objects
            TextView myTitle = (TextView) row.findViewById(R.id.messageRowBodyTextView);

            //Providing the element of an array by specifying its position
            myTitle.setText(messages.get(position));
            return row;
        }


    }

}
