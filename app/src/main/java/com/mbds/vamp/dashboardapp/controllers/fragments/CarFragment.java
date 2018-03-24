package com.mbds.vamp.dashboardapp.controllers.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbds.vamp.dashboardapp.R;
import com.mbds.vamp.dashboardapp.model.Car;
import com.mbds.vamp.dashboardapp.model.User;
import com.mbds.vamp.dashboardapp.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CarFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private OnItemSelectedListener listener;

    TextView charge;
    ImageButton lock;
    boolean state_lock = true;

    ImageButton start;
    boolean state_start = false;

    private String username;
    private String access_token;
    private User loggedUser;
    private List<Car> userCars;
    private ArrayAdapter carArrayAdapter;
    private Spinner spinner;

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
        spinner = (Spinner) getView().findViewById(R.id.spinnerCar);


        //Fetching username and token from shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        access_token = sharedPreferences.getString(Config.ACCESS_TOKEN_SHARED_PREF, "Not Available");

        loggedUser = new User();
        userCars = new ArrayList<Car>();
        System.out.println("lock "+spinner.toString());
        spinner.setOnItemSelectedListener(this);

        getUserCars();

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

    private void getUserCars() {
        AsyncHttpClient client2 = new AsyncHttpClient();
        client2.addHeader("X-Auth-Token", access_token);
        client2.get(Config.USERS_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                super.onSuccess(statusCode, headers, users);
                Log.d("users", users.toString());
                Log.d("statu code : ", String.valueOf(statusCode));

                for (int n = 0; n < users.length(); n++) {
                    try {
                        final JSONObject user = users.getJSONObject(n);
                        if (user != null) {
                            if (user.get("username").toString().equals(username)) {
                                Log.d("loggedUser", user.toString());

                                JSONArray jsonArray = user.getJSONArray("cars");

                                final ArrayList<String> carsIds = new ArrayList<String>();
                                if (jsonArray != null) {
                                    int len = jsonArray.length();
                                    for (int i = 0; i < len; i++) {
                                        carsIds.add(jsonArray.getJSONObject(i).get("id").toString());
                                    }
                                }

                                AsyncHttpClient client3 = new AsyncHttpClient();
                                client3.addHeader("X-Auth-Token", access_token);

                                for (String id : carsIds) {

                                    client3.get(Config.CARS_URL + "/" + id + ".json", null, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject car) {
                                            super.onSuccess(statusCode, headers, car);
                                            Log.d("car", car.toString());
                                            Log.d("statu code : ", String.valueOf(statusCode));

                                            Car userCar = new Car();
                                            try {
                                                userCar.setModel(car.get("model").toString());
                                                userCar.setBrand(car.get("brand").toString());
                                                userCar.setRegisterNumber(car.get("matricule").toString());
                                                userCar.setCharge(car.getInt("charge"));
                                                userCar.setSeatCount(car.getInt("nb_place"));
                                                userCar.setLocked(car.getBoolean("locked"));
                                                //userCar.setAvatar(String.valueOf(car.getJSONObject("images").getInt("id")));
                                                userCars.add(userCar);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            carArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, userCars);
                                            System.out.println("dans le carArray==>"+carArrayAdapter.getItem(0));
                                           spinner.setAdapter(carArrayAdapter);
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                            super.onFailure(statusCode, headers, responseString, throwable);
                                            Log.d("onFailure : ", String.valueOf(statusCode));
                                        }
                                    });
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure : ", String.valueOf(statusCode));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (userCars.size() > 0) {
/*
            model.setText(userCars.get(i).getModel());
            brand.setText(userCars.get(i).getBrand());
            registerNumber.setText(userCars.get(i).getRegisterNumber());*/
            charge.setText(String.valueOf(userCars.get(i).getCharge()));
            state_lock = userCars.get(i).isLocked();
            //TODO seatCount
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    // May also be triggered from the Activity
    public void updateDetail() {
        // create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // Send data to Activity
        listener.onRssItemSelected(newTime);
    }
}

