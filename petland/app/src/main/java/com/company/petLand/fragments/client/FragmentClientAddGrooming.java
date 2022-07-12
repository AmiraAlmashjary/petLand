package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.databinding.FragmentClientNewVetBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Account;
import com.company.petLand.models.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class FragmentClientAddGrooming extends Fragment  {
    private FragmentClientNewVetBinding binding;
    private Booking booking = new Booking();
    String[] services = {"Please select","Hair Cut", "Nail Cut", "Cleaning", "Bathing"};
    String[] times = {"Please select", "6 pm", "7 pm", "8 pm", "9 pm", "10 pm"};
    Account currentAccount;
    boolean isDateSet, isTimeSet, isServiceSet;

    public FragmentClientAddGrooming() {
    }

    public static FragmentClientAddGrooming newInstance() {
        return new FragmentClientAddGrooming();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.getAccountRef().child(Firebase.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentAccount = snapshot.getValue(Account.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientNewVetBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter aa = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, services);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerService.setAdapter(aa);
        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    isServiceSet = true;
                    booking.serviceName = adapterView.getItemAtPosition(i).toString();
                } else {
                    isServiceSet = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isServiceSet = false;
            }
        });


        ArrayAdapter aa2 = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, times);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTime.setAdapter(aa2);
        binding.spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    isTimeSet = true;
                    booking.time = adapterView.getItemAtPosition(i).toString();
                } else {
                    isTimeSet = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isTimeSet = false;
            }
        });


        binding.calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) ->
        {
            isDateSet = true;
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            booking.date = dayOfMonth + "/" + (month + 1) + "/" + year;

        });


        binding.bookBtn.setOnClickListener(view1 -> {
            String key = Firebase.getGroomingBookingsRef().push().getKey();
            booking.id = key;
            booking.account = currentAccount;
            booking.total = 100;
            booking.type="Grooming";
            if (isDateSet && isServiceSet && isTimeSet) {
                FragmentClientBookingPayment fragment = new FragmentClientBookingPayment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("booking",booking);
                fragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment, "Payment")
                        .addToBackStack("Payment")
                        .commit();
            } else {
                Toast.makeText(requireContext(), "Misssing Fields", Toast.LENGTH_SHORT).show();
            }
        });


    }




}