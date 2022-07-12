package com.company.petLand.fragments.admin;

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
import com.company.petLand.databinding.FragmentAdminMoreBinding;
import com.company.petLand.databinding.FragmentClientMoreBinding;
import com.company.petLand.fragments.client.FragmentClientContactUs;
import com.company.petLand.fragments.client.FragmentClientOrders;
import com.company.petLand.fragments.client.FragmentClientProfile;
import com.company.petLand.fragments.client.FragmentClientTermsOfService;
import com.company.petLand.helpers.Firebase;


public class FragmentAdminMore extends Fragment {
    private FragmentAdminMoreBinding binding;

    public FragmentAdminMore() {
    }

    public static FragmentAdminMore newInstance() {
        return new FragmentAdminMore();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminMoreBinding.inflate(LayoutInflater.from(container.getContext()), container, false);


        binding.logOut.setOnClickListener(view -> {
            Firebase.getAuth().signOut();
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        });
        binding.vet.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentAdminVetBookings(), "Vet Bookings")
                    .addToBackStack("Vet Bookings")
                    .commit();
        });

        binding.grooming.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentAdminGroomingBookings(), "Grooming Bookings")
                    .addToBackStack("Grooming Bookings")
                    .commit();
        });

        binding.hotel.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentAdminHotelBookings(), "Hotel Bookings")
                    .addToBackStack("Hotel Bookings")
                    .commit();
        });
        binding.orders.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentAdminOrders(), "Clients Orders")
                    .addToBackStack("Clients Orders")
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