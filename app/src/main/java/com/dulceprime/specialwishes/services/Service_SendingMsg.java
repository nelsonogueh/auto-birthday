package com.dulceprime.specialwishes.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.PrefManager;
import com.dulceprime.specialwishes.other_components.SQLController;
import com.dulceprime.specialwishes.other_components.SomeComponents;


public class Service_SendingMsg extends Service {
    private Handler mHandler;
    private Runnable mRunnable;
    private int mInterval = 1000;

    private static final String TAG = "HelloService";

    private boolean isRunning = false;


    String messageID = "";
    String theReceiverNumber  = "";
    String theMessageBody  = "";
    String theCurrentYear  = "";
    String status  = "";
    String sendingHour  = "";
    String sendingMinute  = "";


    SomeComponents myNewComponents;
    public SQLiteDatabase db;
    public SharedPreferences user_details;
    private String getTableState;

    PrefManager prefManager;

    SQLController sqlController;

//    boolean queryDatabaseForMessages = false;


    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR


        final Handler handler = new Handler(Looper.getMainLooper());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //do your stuff here
//                            queryDatabaseForMessages = true;
                            doTheWholeThing();

                        }
                    });//
                    try {
                        Thread.sleep(60000); // 30 SECONDS
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();


        return Service.START_STICKY;
    }


    public void doTheWholeThing() {
        sqlController = new SQLController(getApplicationContext());

        SomeComponents someComponents = new SomeComponents();
        String nowDay = someComponents.nowDateDay();
        String nowMonth = someComponents.nowDateMonth();
        String nowYear = someComponents.nowDateYear();
        String nowHour = someComponents.nowDateHour();
        String nowMinute = someComponents.nowDateMinute();

        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);

        // QUERY UNSENT MESSAGES
        Cursor c = db.rawQuery("SELECT * FROM " + DBhelper.MESSAGE_SENDING_TABLE + " WHERE " + DBhelper.MESSAGE_SENDING_MONTH + " = '" + nowMonth + "' AND " + DBhelper.MESSAGE_SENDING_DAY + " = '" + nowDay + "'", null);

        if (c.moveToNext()) {
//            Toast.makeText(Service_SendingMsg.this, "Birthday Present Today", Toast.LENGTH_LONG).show();
            // Send the messages one after the other (No while loop). Use defined Thread sleep interval
             messageID = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_ID));
             theReceiverNumber = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_RECIPENT));
             theMessageBody = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_BODY));
             theCurrentYear = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_YEAR));
             status = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_STATUS));
             sendingHour = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_HOUR));
             sendingMinute = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_MINUTE));
//

            // SEND THE TEXT MESSAGE IF THERE'S VALUE TO SEND
            if (status.equalsIgnoreCase(DBhelper.SENDING_STATUS_UNSENT) || status.equalsIgnoreCase(DBhelper.SENDING_STATUS_ATTEMPTED)) {
                // If there is either unsent or attempted message status on db table
                if (sendingHour.trim().length() > 0) { // The user set time to send the message

                    int dbHour = Integer.parseInt(sendingHour);
                    int dbMinute = Integer.parseInt(sendingMinute);
                    int deviceHour = Integer.parseInt(nowHour);
                    int deviceMinute = Integer.parseInt(nowMinute);


                    if ((deviceHour >= dbHour) && (deviceMinute >= dbMinute)) {
                        // Don't send the message before time; but you can send it after the time.
                        sendTextMessage(messageID, theReceiverNumber, theMessageBody);  // This sends the text message to the phone number and updates the table
                    } else {
                        // What to do before the time
                    }
                } else {
                    // User did not set time to send message
                    sendTextMessage(messageID, theReceiverNumber, theMessageBody);  // This sends the text message to the phone number and updates the table
                }
            }
        }
        c.close();
        db.close();

    }

    // this method sends the text message
    public void sendTextMessage(final String sendingMessageRowId, final String phoneNumber, final String smsBody) {
        String SMS_SENT = "SMS_SENT";
        String SMS_DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(Service_SendingMsg.this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(Service_SendingMsg.this, 0, new Intent(SMS_DELIVERED), 0);

// When the SMS has been sent
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        Toast.makeText(Service_SendingMsg.this, "Birthday message sent", Toast.LENGTH_LONG).show();
                        // UPDATE THE TABLE THAT THE MESSAGE HAS BEEN SENT
                        int currentYearInt = Integer.parseInt(theCurrentYear);
                        int nextYear = (currentYearInt+1);  // Incrementing the year
                        String newMessage = "";

                        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);
                        Cursor c1 = db.rawQuery("SELECT * FROM " + DBhelper.BIRTHDAY_MESSAGES_TABLE + " ORDER BY RANDOM()", null);

                        if (c1.moveToNext()) {
//                            Toast.makeText(Service_SendingMsg.this, "Selected new message", Toast.LENGTH_SHORT).show();
                            // Fetching a new message
                            newMessage = c1.getString(c1.getColumnIndex(DBhelper.MESSAGE_BODY));
                        }
                        c1.close();
                        db.close();


                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_SENT);
                        contentValues.put(DBhelper.MESSAGE_SENDING_YEAR, nextYear+"");
                        contentValues.put(DBhelper.MESSAGE_SENDING_BODY, newMessage);

                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);


                        // INSERT INTO MESSAGE SENT HISTORY TABLE
                            ContentValues contentValue2 = new ContentValues();
                            contentValue2.put(DBhelper.HISTORY_ID, messageID);
                            contentValue2.put(DBhelper.HISTORY_SENDING_RECIPENT, theReceiverNumber);
                            contentValue2.put(DBhelper.HISTORY_SENDING_BODY, theMessageBody);
                            contentValue2.put(DBhelper.HISTORY_SENDING_YEAR, theCurrentYear);
                            contentValue2.put(DBhelper.HISTORY_SENDING_STATUS, status);
                            contentValue2.put(DBhelper.HISTORY_SENDING_HOUR, sendingHour);
                            contentValue2.put(DBhelper.HISTORY_SENDING_MINUTE, sendingMinute);

                            sqlController.insertNewRecord(DBhelper.ALREADY_SENT_HISTORY_TABLE, contentValue2);

                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // no airtime
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_ATTEMPTED);
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues2, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);
//                        Toast.makeText(Service_SendingMsg.this, "Birthday message attempted, but unable to send", Toast.LENGTH_LONG).show();

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // no service
                        ContentValues contentValues3 = new ContentValues();
                        contentValues3.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_ATTEMPTED);
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues3, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);
//                        Toast.makeText(Service_SendingMsg.this, "Birthday message attempted, but unable to send", Toast.LENGTH_LONG).show();

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        ContentValues contentValues4 = new ContentValues();
                        contentValues4.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_ATTEMPTED);
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues4, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);
//                        Toast.makeText(Service_SendingMsg.this, "Birthday message attempted, but unable to send", Toast.LENGTH_LONG).show();

//                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // network turned off
                        ContentValues contentValues5 = new ContentValues();
                        contentValues5.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_ATTEMPTED);
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues5, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);
//                        Toast.makeText(Service_SendingMsg.this, "Birthday message attempted, but unable to send", Toast.LENGTH_LONG).show();

//                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        // SUBMIT ERROR
                        ContentValues contentValues6 = new ContentValues();
                        contentValues6.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_ATTEMPTED);
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues6, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);
//                        Toast.makeText(Service_SendingMsg.this, "Birthday message attempted, but unable to send", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SMS_SENT));

// Get the default instance of SmsManager
        SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
        smsManager.sendTextMessage(phoneNumber, null, smsBody, sentPendingIntent, deliveredPendingIntent);


    }



   /* int scheduledYearInteger = Integer.parseInt(theScheduledYear);
    int theSendingMonthInteger = Integer.parseInt(theSendingMonth);*/


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }


}