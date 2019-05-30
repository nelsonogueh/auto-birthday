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


    SomeComponents myNewComponents;
    public SQLiteDatabase db;
    public SharedPreferences user_details;
    private String getTableState;

    PrefManager prefManager;

    SQLController sqlController;


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

//                            Log.d("ECHO ", "SOMETHING");

                            doTheWholeThing();

                        }
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();


        return Service.START_STICKY;
    }


    //TODO: Use shared preference to query if the table has been created first before trying to access the table


    public void doTheWholeThing() {
        sqlController = new SQLController(getApplicationContext());

        SomeComponents someComponents = new SomeComponents();

        String nowDay = someComponents.nowDateDay();

        String nowMonth = someComponents.nowDateMonth();

        String nowYear = someComponents.nowDateYear();

        db = openOrCreateDatabase(DBhelper.DB_NAME, MODE_PRIVATE, null);

        Cursor c = db.rawQuery("SELECT * FROM " + DBhelper.MESSAGE_SENDING_TABLE + " WHERE " + DBhelper.MESSAGE_SENDING_MONTH + " = '" + nowMonth + "' AND " + DBhelper.MESSAGE_SENDING_DAY + " = '" + nowDay + "' AND " + DBhelper.MESSAGE_SENDING_STATUS + " = '" + DBhelper.SENDING_STATUS_UNSENT + "'", null);

        int i = 1;
        while (c.moveToNext()) {

            String messageID = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_ID));
            String theReceiverNumber = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_RECIPENT));
            String theMessageBody = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_BODY));
            String theCurrentYear = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_YEAR));
            String status = c.getString(c.getColumnIndex(DBhelper.MESSAGE_SENDING_STATUS));


            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RECIPIENT: ").append(theReceiverNumber);
            stringBuilder.append("\n");
            stringBuilder.append("MESSAGE: ").append(theMessageBody);
            stringBuilder.append("\n");
            stringBuilder.append("STATUS: ").append(status);
            stringBuilder.append("\n");
            stringBuilder.append("YEAR: ").append(theCurrentYear);
            stringBuilder.append("\n");

            Toast.makeText(Service_SendingMsg.this, stringBuilder, Toast.LENGTH_LONG).show();



//                sendTextMessage(messageID,theReceiverNumber, theMessageBody);  // This sends the text message to the phone number and updates the table

            i++;
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

                        Log.i("", "---------------------------------------------------------------------------------------------------------------------------------------");
                        Log.i("TEXT MESSAGE SENT", "TEXT MESSAGE HAS BEEN SENT TO: " + phoneNumber);
//
                        // UPDATE THE TABLE THAT THE MESSAGE HAS BEEN SENT
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBhelper.MESSAGE_SENDING_STATUS, "sent");
                        sqlController.updateTable(DBhelper.MESSAGE_SENDING_TABLE, contentValues, DBhelper.MESSAGE_SENDING_ID, sendingMessageRowId);


                        //TODO: After updating the table as sent, insert new messageSchedule for the user into the table. Note, note birthday schedule; just message schedule
//                        In otherwords, messageBody, day, month, year = currentYear+1, same recipient etc.

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // no airtime
                        Log.i("", "---------------------------------------------------------------------------------------------------------------------------------------");
                        Log.i("NO AIRTIME", "SORRY YOU DON'T HAVE AIRTIME TO SEND MESSAGE");
//                        updateSentMessagesTable(phoneNumber, smsBody, nowDateDay(), nowDateMonth(), nowDateYear());
//                        Toast.makeText(context, "It seems you don't have airtime", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // no service
//                        Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // network turned off
//                        Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        // SUBMIT ERROR
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