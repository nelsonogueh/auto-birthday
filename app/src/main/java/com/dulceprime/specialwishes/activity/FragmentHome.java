package com.dulceprime.specialwishes.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dulceprime.specialwishes.R;
import com.dulceprime.specialwishes.services.Service_SendingMsg;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    FloatingActionButton addEventHomeFAB;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout birthdayLink = (LinearLayout)view.findViewById(R.id.birthday_link);
        birthdayLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Birthday.class);
                startActivity(intent);
            }
        });


        addEventHomeFAB = (FloatingActionButton)view.findViewById(R.id.homefab_addEvent);
        addEventHomeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),AddEvent.class);
                startActivity(intent);
            }
        });

        return view;

    }

}
