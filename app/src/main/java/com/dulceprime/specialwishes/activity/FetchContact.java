package com.dulceprime.specialwishes.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dulceprime.specialwishes.R;

import java.util.ArrayList;
import java.util.List;

public class FetchContact extends AppCompatActivity {

    private ListView fetchContactListView;
    //    ArrayList contactsArrayList = null;
    List contactNames = null;
    List contactNumbers = null;
    Cursor c;
//    MyFetchContactAdapter fetchContactAdapter;
    ArrayAdapter<String> fetchContactAdapter;

    SearchView inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_contact);

        showContacts();  // Get the contacts before setting them


        fetchContactListView = (ListView) findViewById(R.id.fetchContactListView);
        fetchContactAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contactNames);
//        fetchContactAdapter = new MyFetchContactAdapter(this,contactNames);
//        fetchContactAdapter = new MyFetchContactAdapter(this,contactNames,contactNumbers);

        fetchContactListView.setAdapter(fetchContactAdapter);
        fetchContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                Bundle resultBundle = new Bundle();
                resultBundle.putString("contactResult", contactNumbers.get(i).toString());
                intent.putExtras(resultBundle);
//                setResult(RESULT_OK, intent);
                setResult(120, intent);
                finish();
            }
        });



        inputSearch = (SearchView) findViewById(R.id.contactSearchView);
        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FetchContact.this.fetchContactAdapter.getFilter().filter(newText);
//                fetchContactAdapter.clear();
//                fetchContactAdapter.notify();

                fetchContactAdapter.setNotifyOnChange(true);
                fetchContactAdapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void showContacts() {
        c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

//        contactsArrayList = new ArrayList();
        contactNames = new ArrayList();
        contactNumbers = new ArrayList();
        while (c.moveToNext()) {

            String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactNames.add(contactName);
            contactNumbers.add(phNumber);
        }
        c.close();
    }






    // ADAPTER CLASS
    class MyFetchContactAdapter extends ArrayAdapter {

        List<String> names;
//        List<String> numbers;

        public MyFetchContactAdapter(Context context, List<String> names) {
            //Overriding Default Constructor off ArratAdapter
            super(context, R.layout.fetch_contact_row, names);

            this.names = names;
//            this.numbers = numbers;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Inflating the layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.fetch_contact_row, parent, false);

            //Get the reference to the view objects
            TextView name = (TextView) row.findViewById(R.id.contactName);
//            TextView number = (TextView) row.findViewById(R.id.contactNumber);

            //Providing the element of an array by specifying its position
            name.setText(names.get(position)+"");
//            number.setText(numbers.get(position)+"");
            return row;
        }


    }










/*


    // ADAPTER CLASS
    class MyFetchContactAdapter extends ArrayAdapter {

        List<String> names;
        List<String> numbers;

        public MyFetchContactAdapter(Context context, List<String> names, List<String> numbers) {
            //Overriding Default Constructor off ArratAdapter
            super(context, R.layout.fetch_contact_row, numbers);

            this.names = names;
            this.numbers = numbers;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Inflating the layout
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.fetch_contact_row, parent, false);

            //Get the reference to the view objects
            TextView name = (TextView) row.findViewById(R.id.contactName);
            TextView number = (TextView) row.findViewById(R.id.contactNumber);

            //Providing the element of an array by specifying its position
            name.setText(names.get(position)+"");
            number.setText(numbers.get(position)+"");
            return row;
        }


    }
*/

}
