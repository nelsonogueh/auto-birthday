package com.dulceprime.specialwishes.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.other_components.DBhelper;
import com.dulceprime.specialwishes.other_components.SQLController;
import com.dulceprime.specialwishes.other_components.SomeComponents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddEvent extends AppCompatActivity {

    TextInputEditText inputPhoneNumber, dateET, add_timeET, messageET;
    ImageButton contactButton;
    RadioGroup messageOptionRG;
    Button addEvent_submitBT;
    TextView messageCharacterCounterTV;
    String activityResultReturned = null;
    SomeComponents myComponent;

    ArrayList<String> messagesList;
    private SQLController db;

    ConstraintLayout parentLayout;
    private SQLiteDatabase db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        myComponent = new SomeComponents();


        db = new SQLController(this);
        db.open();


        parentLayout = (ConstraintLayout) findViewById(R.id.parentConstriantAddEvent);
        inputPhoneNumber = (TextInputEditText) findViewById(R.id.addEntry_contactET);
        contactButton = (ImageButton) findViewById(R.id.addEntry_contactButton);
        dateET = (TextInputEditText) findViewById(R.id.add_dateET);
        add_timeET = (TextInputEditText) findViewById(R.id.add_timeET);
        messageOptionRG = (RadioGroup) findViewById(R.id.messageOptionRG);
        messageET = (TextInputEditText) findViewById(R.id.messageET);
        messageCharacterCounterTV = (TextView) findViewById(R.id.messageCharacterCounterTV);
        addEvent_submitBT = (Button) findViewById(R.id.addEvent_submitBT);


        // Just setting random message to the message box for initialization
        initializeRandomMessage();


        inputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myComponent.phoneInputType = "typed";  // If the user change anything on the input field, we set it to typed.
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Date edittext
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Pick birth date");
                dFragment.setCancelable(true);
            }
        });
        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // Initialize a new date picker dialog fragment
                    DialogFragment dFragment = new DatePickerFragment();

                    // Show the date picker dialog fragment
                    dFragment.show(getFragmentManager(), "Pick birth date");
                    dFragment.setCancelable(true);
                }
            }
        });

        add_timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new time picker dialog fragment
                DialogFragment dFragment = new TimePickerFragment();

                // Show the time picker dialog fragment
                dFragment.show(getFragmentManager(), "Sending time");
            }
        });
        add_timeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // Initialize a new time picker dialog fragment
                    DialogFragment dFragment = new TimePickerFragment();

                    // Show the time picker dialog fragment
                    dFragment.show(getFragmentManager(), "Sending time");
                }
            }
        });
        RadioButton randomRadioButton = (RadioButton) findViewById(R.id.addEvent_randomMsg);
        randomRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageET.setText("");
                messageET.setText(fetchDBMessage().get(myComponent.randomNumber(0, (fetchDBMessage().size() - 1))));
            }
        });

        messageOptionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.addEvent_randomMsg:
                        messageET.setText("");
                        messageET.setText(fetchDBMessage().get(myComponent.randomNumber(0, (fetchDBMessage().size() - 1))));
                        break;

                    case R.id.addEvent_enterMsg:
                        messageET.setText("");
                        break;
                }
            }
        });

        RadioButton selectMessageRD = (RadioButton) findViewById(R.id.addEvent_selectMsg);
        selectMessageRD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMessageFromResult();
            }
        });


        messageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                messageCharacterCounterTV.setText(myComponent.returnSMSCharacterCount(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEvent.this, FetchContact.class);
                startActivityForResult(intent, 0);
            }
        });

        // SUBMIT BUTTON CLICKED
        addEvent_submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SomeComponents.finalMessageBody = messageET.getText().toString().trim();
                validateFieldsAndInsertScheduleToDB(); // Validate all the fields first. And do the whole insert to database
            }
        });
    }


    private void validateFieldsAndInsertScheduleToDB() {
        // VALIDATE PHONE NUMBER INPUT FIELD
        if (SomeComponents.phoneInputType.equalsIgnoreCase("none") || SomeComponents.finalPhoneNumber.trim().equalsIgnoreCase("")) {
            inputPhoneNumber.setError("Please enter or select a valid contact");
            return;
        } else {
            if (SomeComponents.phoneInputType.equalsIgnoreCase("typed")) {
                SomeComponents.finalPhoneNumber = myComponent.removeSpaceAndHyphen(inputPhoneNumber.getText().toString().trim());
            }
        }

        // VALIDATE DATE INPUT FIELD
        if (SomeComponents.finalBirthMonth.trim().equalsIgnoreCase("") || SomeComponents.finalBirthDay.trim().equalsIgnoreCase("")) {
            dateET.setError("Please select a valid date");
            return;
        }

        // VALIDATE MESSAGE INPUT FIELD
        if (SomeComponents.finalMessageBody.trim().equalsIgnoreCase("")) {
            messageET.setError("Sorry! The message field is empty. Please type or select message.");
            return;
        }

        // INSERT TO SCHEDULED BIRTHDAY TABLE
        ContentValues contentValue2 = new ContentValues();
        contentValue2.put(DBhelper.SCHEDULED_RECIPENT, SomeComponents.finalPhoneNumber);
        contentValue2.put(DBhelper.SCHEDULED_DAY, SomeComponents.finalBirthDay);
        contentValue2.put(DBhelper.SCHEDULED_MONTH, SomeComponents.finalBirthMonth);
        contentValue2.put(DBhelper.SCHEDULED_YEAR, myComponent.nowDateYear());
        contentValue2.put(DBhelper.SCHEDULED_SENDING_HOUR, SomeComponents.finalSendingTimeHour);
        contentValue2.put(DBhelper.SCHEDULED_SENDING_MINUTE, SomeComponents.finalSendingTimeMinute);

        // Inserting the values into the scheduled birthday table
        db.insertNewRecord(DBhelper.SCHEDULED_BIRTHDAY_TABLE, contentValue2);


        // INSERT INTO MESSAGE SENDING TABLE
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBhelper.MESSAGE_SENDING_RECIPENT, SomeComponents.finalPhoneNumber);
        contentValue.put(DBhelper.MESSAGE_SENDING_BODY, SomeComponents.finalMessageBody);
        contentValue.put(DBhelper.MESSAGE_SENDING_DAY, SomeComponents.finalBirthDay);
        contentValue.put(DBhelper.MESSAGE_SENDING_MONTH, SomeComponents.finalBirthMonth);
        contentValue.put(DBhelper.MESSAGE_SENDING_HOUR, SomeComponents.finalSendingTimeHour);
        contentValue.put(DBhelper.MESSAGE_SENDING_MINUTE, SomeComponents.finalSendingTimeMinute);
        contentValue.put(DBhelper.MESSAGE_SENDING_STATUS, DBhelper.SENDING_STATUS_UNSENT);
        contentValue.put(DBhelper.MESSAGE_SENDING_YEAR, myComponent.nowDateYear());

        db.insertNewRecord(DBhelper.MESSAGE_SENDING_TABLE, contentValue);

//        myComponent.showSnackbarMessage(parentLayout, "Birthday scheduled successfully!");
        Toast.makeText(AddEvent.this, "Birthday scheduled successfully!", Toast.LENGTH_SHORT).show();

        // MAKING THE VARIABLES EMPTY AFTER INSERTING TO DB
        SomeComponents.finalPhoneNumber = ""; //        SomeComponents.finalBirthMonth = ""; //
        SomeComponents.finalBirthDay = ""; //
        SomeComponents.finalMessageBody = ""; //
        SomeComponents.finalSendingTimeHour = ""; //
        SomeComponents.finalSendingTimeMinute = ""; //


        // CLEARING THE INPUT FIELDS
        dateET.setText("");
        add_timeET.setText("");
        inputPhoneNumber.setText("");

        initializeRandomMessage();

    }

    private void selectMessageFromResult() {
        Intent intent = new Intent(AddEvent.this, SelectMessageResult.class);
        intent.putExtra("RequestType", "SELECT_MESSAGE");
        startActivityForResult(intent, 0);
    }

    private void initializeRandomMessage() {
        // Set random message to Message EditText by default
        messageET.setText("");
        messageET.setText(fetchDBMessage().get(myComponent.randomNumber(0, (fetchDBMessage().size() - 1))));
        messageCharacterCounterTV.setText(myComponent.returnSMSCharacterCount(messageET.getText().toString()));  // Count the default message
    }


    public ArrayList<String> fetchDBMessage() {
        messagesList = new ArrayList<String>();

        DBhelper dBhelper = new DBhelper(this);
        db1 = dBhelper.getWritableDatabase();
        Cursor c = db1.rawQuery("SELECT * FROM " + DBhelper.BIRTHDAY_MESSAGES_TABLE + " ORDER BY " + DBhelper.MESSAGE_ID + " DESC", null);

        while (c.moveToNext()) {
            // Populating the List
            messagesList.add(c.getString(c.getColumnIndex(DBhelper.MESSAGE_BODY)));
        }

        c.close();
        db1.close();


        return messagesList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle resultBundle = data.getExtras();
            assert resultBundle != null;
            String result = resultBundle.getString("resultMessage");
            messageET.setText(""); // clear the edit text first
            messageET.setText(result); // set the result now
            messageCharacterCounterTV.setText(myComponent.returnSMSCharacterCount(result));

        } else if (resultCode == 120) {
            Bundle resultBundle = data.getExtras();
            assert resultBundle != null;
            String resultPhoneNumber = resultBundle.getString("contactResult");
            inputPhoneNumber.setText(""); // clear the edit text first
            inputPhoneNumber.setText(myComponent.getContactName(this, myComponent.removeSpaceAndHyphen(resultPhoneNumber))); // set the result now

            myComponent.finalPhoneNumber = myComponent.removeSpaceAndHyphen(resultPhoneNumber);
            myComponent.phoneInputType = "selected";
            ;
        }

    }


    // DATE PICKER CLASS
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        EditText dateET;
        SomeComponents myComponent;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            dateET = (EditText) getActivity().findViewById(R.id.add_dateET);
            myComponent = new SomeComponents();

            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();


            SimpleDateFormat dateDayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat dateMonthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat dateYearFormat = new SimpleDateFormat("yyyy");


            String dateDay = dateDayFormat.format(chosenDate);
            String dateMonth = dateMonthFormat.format(chosenDate);
            String dateYear = dateYearFormat.format(chosenDate);

            SomeComponents.finalBirthDay = dateDay;
            SomeComponents.finalBirthMonth = dateMonth;

            // Display the chosen date to app interface
            dateET.setText(SomeComponents.dateMonthName(dateMonth) + " " + SomeComponents.dateDaySuffix(dateDay));
        }
    }

    // TIME PICKER CLASS
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        EditText timeET;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            // Create a TimePickerDialog with current time
            TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, false);
            // Return the TimePickerDialog
            return tpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeET = (EditText) getActivity().findViewById(R.id.add_timeET);
            timeET.setText(hourOfDay + ":" + minute);

            SomeComponents.finalSendingTimeHour = hourOfDay + "";
            SomeComponents.finalSendingTimeMinute = minute + "";
        }
    }
}
