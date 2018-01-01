package com.mbds.vamp.dashboardapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mbds.vamp.dashboardapp.R;

public class LoginActivity extends AppCompatActivity {

    public static boolean LOGIN = false;

    Button connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       System.out.println("**********login***********''"+R.layout.activity_login);
        setContentView(R.layout.activity_login);

        connexion = (Button) findViewById(R.id.login_button);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGIN = true;
                Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });
    }
}
