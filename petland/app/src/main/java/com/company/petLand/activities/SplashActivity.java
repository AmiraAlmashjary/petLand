package com.company.petLand.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.petLand.R;
import com.company.petLand.fragments.admin.ActivityAdminMain;
import com.company.petLand.fragments.client.ActivityClientMain;
import com.company.petLand.fragments.registeration.FragmentWelcome;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnBackClickedLinstener;
import com.company.petLand.models.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
              startActivity(mainIntent);
                finish();
            }
        }, 3000);
    }



}