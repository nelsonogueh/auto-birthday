package com.dulceprime.specialwishes.other_components;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    // Database Information
    public static final String DB_NAME = "MySQLITEDB.DB";

    // database version
    static final int DB_VERSION = 1;

    public static final String SENDING_STATUS_SENT = "sent";
    public static final String SENDING_STATUS_UNSENT = "unsent";
    public static final String SENDING_STATUS_ATTEMPTED = "attempted";

    /**
     * SCHEDULED BIRTHDAY TABLE
     **/
    // SCHEDULED BIRTHDAY TABLE NAME
    public static final String SCHEDULED_BIRTHDAY_TABLE = "birthday_schedule";
    // Declaring the field for SCHEDULED BIRTHDAY TABLE COLUMNS
    public static final String SCHEDULED_ID = "schedule_id", SCHEDULED_RECIPENT = "recipient_number", SCHEDULED_DAY = "birth_day", SCHEDULED_MONTH = "birth_month", SCHEDULED_YEAR = "scheduled_year", SCHEDULED_SENDING_HOUR = "scheduled_sending_hour", SCHEDULED_SENDING_MINUTE = "scheduled_sending_minute";
    // Creating scheduled birthday table query
    private static final String CREATE_SCHEDULED_BIRTHDAY_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEDULED_BIRTHDAY_TABLE + "(" + SCHEDULED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCHEDULED_RECIPENT + " VARCHAR, " + SCHEDULED_DAY + " VARCHAR, " + SCHEDULED_MONTH + " VARCHAR, " + SCHEDULED_YEAR + " VARCHAR, " + SCHEDULED_SENDING_HOUR + " VARCHAR, " + SCHEDULED_SENDING_MINUTE + " VARCHAR)";





// BIRTHDAY MESSAGES TABLE NAME
    public static final String BIRTHDAY_MESSAGES_TABLE = "birthday_message";
    // Declaring the field for BIRTHDAY MESSAGES TABLE COLUMNS
    public static final String MESSAGE_ID = "Message_Id", MESSAGE_TYPE = "Message_Type", MESSAGE_BODY = "Message_Body", ONLINE_UNIQUE_ID = "Online_Unique_Id";
    // Creating birthday messages table query
    private static final String CREATE_BIRTHDAY_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + BIRTHDAY_MESSAGES_TABLE + "(" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MESSAGE_TYPE + " VARCHAR, " + MESSAGE_BODY + " LONGTEXT, " + ONLINE_UNIQUE_ID + " VARCHAR)";

//    BIRTHDAY_MESSAGES



    /**
     * SENDING MESSAGE TABLE
     **/
    // SENDING MESSAGE TABLE NAME
    public static final String MESSAGE_SENDING_TABLE = "message_sending";
    // Declaring the field for SENDING MESSAGE TABLE COLUMNS
    public static final String MESSAGE_SENDING_ID = "message_sending_id", MESSAGE_SENDING_RECIPENT = "message_recipient", MESSAGE_SENDING_BODY = "message_body", MESSAGE_SENDING_HOUR = "message_time_hour", MESSAGE_SENDING_MINUTE = "message_time_minute", MESSAGE_SENDING_STATUS = "message_status", MESSAGE_SENDING_YEAR = "message_year", MESSAGE_SENDING_DATE_SENT = "message_sent_time", MESSAGE_SENDING_DAY = "message_birth_day", MESSAGE_SENDING_MONTH = "message_birth_month";
    // Creating scheduled birthday table query
    private static final String CREATE_MESSAGE_SENDING_TABLE = "CREATE TABLE IF NOT EXISTS " + MESSAGE_SENDING_TABLE + "(" + MESSAGE_SENDING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MESSAGE_SENDING_RECIPENT + " VARCHAR, " + MESSAGE_SENDING_BODY + " LONGTEXT, " + MESSAGE_SENDING_HOUR + " VARCHAR, " + MESSAGE_SENDING_MINUTE + " VARCHAR, " + MESSAGE_SENDING_STATUS + " VARCHAR, " + MESSAGE_SENDING_YEAR + " VARCHAR," + MESSAGE_SENDING_DATE_SENT + " VARCHAR," + MESSAGE_SENDING_DAY + " VARCHAR," + MESSAGE_SENDING_MONTH + " VARCHAR)";


    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the database tables
        db.execSQL(CREATE_SCHEDULED_BIRTHDAY_TABLE);
        db.execSQL(CREATE_MESSAGE_SENDING_TABLE);
        db.execSQL(CREATE_BIRTHDAY_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULED_BIRTHDAY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_SENDING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_SENDING_TABLE);
        onCreate(db);
    }
}