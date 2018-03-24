package com.mbds.vamp.dashboardapp.controllers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mbds.vamp.dashboardapp.R;
import com.mbds.vamp.dashboardapp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.params.ClientPNames;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean loggedIn = false;

    Button connexion;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        connexion = (Button) findViewById(R.id.login_button);

        connexion.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        if (loggedIn) {
            String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "");
            String password = sharedPreferences.getString(Config.PASSWORD_SHARED_PREF, "");
            try {
                login(username, password);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
       // startActivity(new Intent(LoginActivity.this, AppActivity.class));
       try {
            login(usernameEditText.getText().toString(), passwordEditText.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* Intent intent = new Intent(LoginActivity.this, AppActivity.class);
        startActivity(intent);*/
    }

    private void login(final String username, final String password) throws JSONException, IOException {
        final AsyncHttpClient client = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("username", username);
        jsonParams.put("password", password);

        StringEntity entity = new StringEntity(jsonParams.toString());
        Log.d("entity", entity.getContent().toString());
        Log.d("jsonParams", jsonParams.toString());


        client.setEnableRedirects(true);
        client.getHttpClient().getParams().setParameter(ClientPNames.MAX_REDIRECTS, 3);
        System.out.println("bonjour===>"+getApplicationContext()+ Config.LOGIN_URL+
                entity+ ContentType.APPLICATION_JSON.getMimeType());
        client.post(getApplicationContext(), Config.LOGIN_URL, entity, ContentType.APPLICATION_JSON.getMimeType(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("response", response.toString());
                Log.d("statu code : ", String.valueOf(statusCode));
                try {
                    final String username = response.get("username").toString();
                    final String access_token = response.get("access_token").toString();
                    System.out.println("username===> "+username);
                    System.out.println("access token==>"+access_token);
                    //Creating a shared preference
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.USERNAME_SHARED_PREF, username);
                    //editor.putString(Config.PASSWORD_SHARED_PREF, password);
                    editor.putString(Config.ACCESS_TOKEN_SHARED_PREF, access_token);
                    //Saving values to editor
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, AppActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("onFailure : ", String.valueOf(statusCode));
                //TODO message en foction de status code
                Toast.makeText(getApplicationContext(), "Nom d'utilisateur ou mot de passe invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
