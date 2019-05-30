package com.dulceprime.specialwishes.other_components;

/**
 * Created by Nelson on 3/25/2019.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLController {

    private DBhelper DBhelper;
    private Context ourcontext;
    public SQLiteDatabase db;

    public SQLController(Context c) {
        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        DBhelper = new DBhelper(ourcontext);
        db = DBhelper.getWritableDatabase();
        return this;

    }

    public void close() {
        DBhelper.close();
    }

/*
    public Cursor fetchAllScheduledBirthday() {
        String[] columns = new String[] { DBhelper._ID, DBhelper.TODO_SUBJECT,
                DBhelper.TODO_DESC };
        Cursor cursor = db.query(DBhelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
*/

    public int updateScheduledBirthday(long schedule_id, String phoneNumber, String day, String month, String sendingHour, String sendingMinute) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBhelper.SCHEDULED_RECIPENT, phoneNumber);
        contentValue.put(DBhelper.SCHEDULED_DAY, day);
        contentValue.put(DBhelper.SCHEDULED_MONTH, month);
        contentValue.put(DBhelper.SCHEDULED_SENDING_HOUR, sendingHour);
        contentValue.put(DBhelper.SCHEDULED_SENDING_MINUTE, sendingMinute);

        int i = db.update(DBhelper.SCHEDULED_BIRTHDAY_TABLE, contentValue,
                DBhelper.SCHEDULED_ID + " = " + schedule_id, null);
        return i;
    }


    /*public void updateMessageSendingTable(ContentValues fieldsAndValues, String rowID) {
        db.update(DBhelper.MESSAGE_SENDING_TABLE, fieldsAndValues, DBhelper.MESSAGE_SENDING_ID + " = " + rowID, null);
    } */

    public int updateTable(String tableName, ContentValues fieldsAndValues, String dbColumnIDName,String rowIdValue) {

        DBhelper = new DBhelper(ourcontext);
        db = DBhelper.getWritableDatabase();

        int i = db.update(tableName, fieldsAndValues,
                dbColumnIDName + " = " + rowIdValue, null);
        return i;
    }


    public void deleteScheduledBirthday(long schedule_id) {
        db.delete(DBhelper.SCHEDULED_BIRTHDAY_TABLE, DBhelper.SCHEDULED_ID + "=" + schedule_id, null);
    }





    public void deleteItemFromTable(String tableName, String primaryKeyColumnName, String rowValueID) {
        DBhelper = new DBhelper(ourcontext);
        db = DBhelper.getWritableDatabase();

        db.delete(tableName, primaryKeyColumnName + "=" + rowValueID, null);
        // Example below
//        db.delete(DBhelper.SCHEDULED_BIRTHDAY_TABLE, DBhelper.SCHEDULED_ID + "=" + schedule_id, null);
    }




    // INSERT NEW RECORD TABLE
    public void insertNewRecord(String tableName, ContentValues columnAndValues) {
        DBhelper = new DBhelper(ourcontext);
        db = DBhelper.getWritableDatabase();

//        ContentValues contentValue = new ContentValues();
//        contentValue.put(DBhelper.SCHEDULED_RECIPENT, phoneNumber);

        db.insert(tableName, null, columnAndValues);
    }


    //TODO: Use pass content value as an argument to the method that will update the table so that only what we want to update will be updated


    public ArrayList<String> getAllStoredMessagesFromDB() {
        Cursor c = db.rawQuery("SELECT * FROM system_birthday_message", null);
        ArrayList<String> messagesList = new ArrayList<String>();

        while (c.moveToNext()) {
            // Populating the List
            messagesList.add(c.getString(c.getColumnIndex("message_body")));
        }

        c.close();
        db.close();
        return messagesList;
    }

    public String[] getOnlyOneRandomRowFromMessageTable() {
        // This method gets only one row at random and gives the whole row values as an array
        // to get the message body, call the method like this getOnlyOneRandomRowFromMessageTable()[2];
        String[] randomMessage = new String[4];
        Cursor c = db.rawQuery("SELECT * FROM system_birthday_message ORDER BY RANDOM() LIMIT 1", null);
        if (c.moveToNext()) {
            randomMessage[0] = c.getString(c.getColumnIndex("Message_Id"));
            randomMessage[1] = c.getString(c.getColumnIndex("Message_Type"));
            randomMessage[3] = c.getString(c.getColumnIndex("Online_Unique_Id"));
        }
        c.close();
        db.close();
        return randomMessage;  // WE HAVE TO REMOVE THIS NULL LATER
    }
}