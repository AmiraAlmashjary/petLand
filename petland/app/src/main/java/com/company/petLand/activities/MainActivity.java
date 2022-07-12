package com.company.petLand.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Firebase.getAuth().getCurrentUser() == null) {
            lunchWelcomeFragment();
        } else {
            checkTypeAndRedirect();
        }

    }

    private void checkTypeAndRedirect() {

        Firebase.getAccountRef().child(Firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Account account = snapshot.getValue(Account.class);



                    if (account.type.equals("customer")) {
                        lunchCustomerMain();
                    } else {
                        lunchAdminMain();
                    }

                } else {
                    lunchWelcomeFragment();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void lunchFamilyMain() {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, new FragmentFamilyMain())
//                .commit();
//    }

    private void lunchCustomerMain() {
        startActivity(new Intent(this, ActivityClientMain.class));
        finish();
    }
    private void lunchAdminMain() {
        startActivity(new Intent(this, ActivityAdminMain.class));
        finish();
    }


    private void lunchWelcomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, FragmentWelcome.newInstance(), "welcome")
                .commit();
    }


    @Override
    public void onBackPressed() {

        OnBackClickedLinstener listener = (FragmentWelcome) getSupportFragmentManager().findFragmentByTag("welcome");
        if (listener != null) {
            if (listener.onBackClicked()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}