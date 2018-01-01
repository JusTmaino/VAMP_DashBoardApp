package com.mbds.vamp.dashboardapp.controllers.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mbds.vamp.dashboardapp.R;

public class CarFragment extends Fragment {

    private OnItemSelectedListener listener;

    TextView charge;
    ImageButton lock;
    boolean state_lock = true;

    ImageButton start;
    boolean state_start = false;

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car,
                container, false);
        return view;
    }*/
  private static View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_car, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        charge = (TextView) getView().findViewById(R.id.home_charge);

        lock = (ImageButton) getView().findViewById(R.id.home_lock);
        start = (ImageButton) getView().findViewById(R.id.home_start);


        ////////////////////////////////////////////////////////////////////////////////////////////
        // Gestion de commandes de contrôle
        ////////////////////////////////////////////////////////////////////////////////////////////

       lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state_lock) {
                    state_lock = false;
                    lock.setImageResource(R.drawable.ic_unlock);
                    Snackbar.make(v, R.string.home_unlock, Snackbar.LENGTH_LONG)
                            .setAction(R.string.home_unlock, null).show();
                } else {
                    state_lock = true;
                    lock.setImageResource(R.drawable.ic_lock);
                    Snackbar.make(v, R.string.home_lock, Snackbar.LENGTH_LONG)
                            .setAction(R.string.home_lock, null).show();
                }
            }

        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state_start) {
                    state_start = false;
                    start.setImageResource(R.drawable.ic_start);
                    Snackbar.make(v, R.string.home_stop, Snackbar.LENGTH_LONG)
                            .setAction(R.string.home_stop, null).show();
                } else {
                    state_start = true;
                    start.setImageResource(R.drawable.ic_stop);
                    Snackbar.make(v, R.string.home_start, Snackbar.LENGTH_LONG)
                            .setAction(R.string.home_start, null).show();

                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Initialisation des données du véhicule
        ////////////////////////////////////////////////////////////////////////////////////////////

        charge.setText("99%");

        ////////////////////////////////////////////////////////////////////////////////////////////
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CarFragment.OnItemSelectedListener) {
           listener = (CarFragment.OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet CarFragment.OnItemSelectedListener");
        }
    }


    // May also be triggered from the Activity
    public void updateDetail() {
        // create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // Send data to Activity
        listener.onRssItemSelected(newTime);
    }
}

