package com.company.petLand.fragments.admin;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.company.petLand.R;
import com.company.petLand.databinding.ActivityAdminMainBinding;
import com.company.petLand.fragments.client.FragmentClientPets;


public class ActivityAdminMain extends AppCompatActivity {
    ActivityAdminMainBinding binding;
    final Fragment home = new FragmentAdminShop();
    final Fragment blog = new FragmentAdminBlog();
    final Fragment more = new FragmentAdminMore();

    Fragment active = home;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.container, more, "Admin More").hide(more).commit();
        fm.beginTransaction().add(R.id.container, blog, "Blog Edit").hide(blog).commit();
        fm.beginTransaction().add(R.id.container, home, "Shop Edit").commit();
        binding.toolbar.title.setText("Shop Edit");

//        if (Firebase.cart != null) {
//            binding.toolbar.cartBtn.setVisibility(View.VISIBLE);
//        } else {
//            binding.toolbar.cartBtn.setVisibility(View.GONE);
//        }
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStackImmediate();
            }
            switch (item.getItemId()) {
                case R.id.home:
                    binding.toolbar.title.setText("Shop Edit");
                    fm.beginTransaction().hide(active).show(home).commit();
                    active = home;
                    break;

                case R.id.myblogs:

                    fm.beginTransaction().hide(active).show(blog).commit();
                    active = blog;
                    binding.toolbar.title.setText("Blog Edit");
                    break;
                case R.id.more:
                    binding.toolbar.title.setText("Admin More");
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
            if (f instanceof FragmentAdminMore || f instanceof FragmentAdminShop || f instanceof FragmentAdminBlog) {
                binding.toolbar.backBtn.setVisibility(View.GONE);
            } else {
                binding.toolbar.backBtn.setVisibility(View.VISIBLE);
            }

        });


    }


}