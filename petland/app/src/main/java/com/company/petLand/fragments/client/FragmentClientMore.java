package com.company.petLand.fragments.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.activities.MainActivity;
import com.company.petLand.databinding.FragmentClientMoreBinding;
import com.company.petLand.helpers.Firebase;


public class FragmentClientMore extends Fragment {
    private FragmentClientMoreBinding binding;

    public FragmentClientMore() {
    }

    public static FragmentClientMore newInstance() {
        return new FragmentClientMore();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientMoreBinding.inflate(LayoutInflater.from(container.getContext()), container, false);


        binding.logOut.setOnClickListener(view -> {
            Firebase.getAuth().signOut();
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        });
        binding.contactus.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientContactUs(), "Contact Us")
                    .addToBackStack("profile")
                    .commit();
        });

        binding.profile.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientProfile(), "Profile")
                    .addToBackStack("contactus")
                    .commit();
        });
        binding.bookings.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientBookings(), "My Bookings")
                    .addToBackStack("My Bookings")
                    .commit();
        });
        binding.privacyAndPolicies.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientTermsOfService(), "Privacy Policy")
                    .addToBackStack("privacy")
                    .commit();
        });
        binding.orders.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientOrders(), "My Previous Orders")
                    .addToBackStack("My Previous Orders")
                    .commit();
        });
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}