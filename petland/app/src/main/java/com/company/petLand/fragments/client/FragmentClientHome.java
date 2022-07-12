package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.company.petLand.R;
import com.company.petLand.adapters.ViewPagerAdapter;
import com.company.petLand.databinding.FragmentClientHomeBinding;
import com.company.petLand.fragments.admin.FragmentAdminBlog;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FragmentClientHome extends Fragment {

    private FragmentClientHomeBinding binding;
    private Handler sliderHandler = new Handler();
    Runnable sliderRunnable;
    private ImageView[] dots;
    private Account account;

    public FragmentClientHome() {
    }

    public static FragmentClientHome newInstance() {
        return new FragmentClientHome();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientHomeBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        Firebase.getAccountRef().child(Firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                account = snapshot.getValue(Account.class);
                binding.welcomeTxt.setText("Hello, " + account.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireContext());
        binding.viewPager.setAdapter(viewPagerAdapter);
        int dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(requireContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.sliderdots.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dot));

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 3000);


        binding.shop.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientShop(), "Shop")
                    .addToBackStack("Shop")
                    .commit();
        });

        binding.blog.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientBlog(), "Blog")
                    .addToBackStack("Blog")
                    .commit();
        });
        binding.vet.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientAddVet(), "New Vet Booking")
                    .addToBackStack("New Vet Booking")
                    .commit();
        });
        binding.grooming.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientAddGrooming(), "New Grooming Booking")
                    .addToBackStack("New Grooming Booking")
                    .commit();
        });

        binding.hotel.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientAddHotel(), "New Hotel Booking")
                    .addToBackStack("New Hotel Booking")
                    .commit();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
}