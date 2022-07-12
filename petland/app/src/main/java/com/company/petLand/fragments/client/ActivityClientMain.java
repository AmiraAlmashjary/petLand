package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.company.petLand.R;
import com.company.petLand.databinding.ActivityClientMainBinding;
import com.company.petLand.helpers.Firebase;


public class ActivityClientMain extends AppCompatActivity {
    ActivityClientMainBinding binding;
    final Fragment home = new FragmentClientHome();
    final Fragment myPets = new FragmentClientPets();
    final Fragment more = new FragmentClientMore();

    Fragment active = home;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = ActivityClientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.container, more, "More").hide(more).commit();
        fm.beginTransaction().add(R.id.container, myPets, "My Pets").hide(myPets).commit();
        fm.beginTransaction().add(R.id.container, home, "Home").commit();
        binding.toolbar.title.setText("Home");

        if (Firebase.cart != null) {
            binding.toolbar.cartBtn.setVisibility(View.VISIBLE);
        } else {
            binding.toolbar.cartBtn.setVisibility(View.GONE);
        }
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStackImmediate();
            }
            switch (item.getItemId()) {
                case R.id.home:
                    binding.toolbar.title.setText("Home");
                    fm.beginTransaction().hide(active).show(home).commit();
                    active = home;
                    break;

                case R.id.mypets:

                    fm.beginTransaction().hide(active).show(myPets).commit();
                    active = myPets;
                    binding.toolbar.title.setText("My Pets");
                    break;
                case R.id.more:
                    binding.toolbar.title.setText("More");
                    fm.beginTransaction().hide(active).show(more).commit();
                    active = more;
                    break;
            }

            return true;
        });
        binding.toolbar.backBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
            binding.toolbar.title.setText(f != null ? f.getTag() : null);
            if (f instanceof FragmentClientMore || f instanceof FragmentClientHome || f instanceof FragmentClientPets) {
                binding.toolbar.backBtn.setVisibility(View.GONE);
            } else {
                binding.toolbar.backBtn.setVisibility(View.VISIBLE);
            }


            if (Firebase.cart != null) {

                if (f instanceof FragmentClientHome || f instanceof FragmentClientShop) {
                    binding.toolbar.cartBtn.setVisibility(View.VISIBLE);
                }else {
                    binding.toolbar.cartBtn.setVisibility(View.GONE);
                }

            } else {
                binding.toolbar.cartBtn.setVisibility(View.GONE);
            }


        });
        binding.toolbar.cartBtn.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientCart(), "Cart")
                    .addToBackStack("Cart")
                    .commit();
        });


    }


}