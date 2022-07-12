package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.databinding.FragmentClientPaymentBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Account;
import com.company.petLand.models.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FragmentClientBookingPayment extends Fragment {

    private FragmentClientPaymentBinding binding;
    private Account currentAccount;
    private boolean isEmpty;

    public FragmentClientBookingPayment() {
    }

    public static FragmentClientBookingPayment newInstance() {
        return new FragmentClientBookingPayment();
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
        binding = FragmentClientPaymentBinding.inflate(LayoutInflater.from(container.getContext()), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        Booking booking = bundle.getParcelable("booking");

        binding.price.setText("You will be charged" + " " + 100 + "$");
        binding.confirm.setOnClickListener(view1 -> {

            if (validate()==false) {
                if (booking.type.equals("Hotel")) {
                    Firebase.getHotelBookingsRef().child(booking.id).setValue(booking).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Firebase.getClientBookings(currentAccount.id).child(booking.id).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireActivity(), "You Booked Successfuly", Toast.LENGTH_SHORT).show();
                                        for (int i = 0; i < requireActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                            requireActivity().getSupportFragmentManager().popBackStack();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (booking.type.equals("Grooming")) {
                    Firebase.getGroomingBookingsRef().child(booking.id).setValue(booking).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Firebase.getClientBookings(currentAccount.id).child(booking.id).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireActivity(), "You Booked Successfuly", Toast.LENGTH_SHORT).show();
                                        for (int i = 0; i < requireActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                            requireActivity().getSupportFragmentManager().popBackStack();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (booking.type.equals("Vet")) {
                    Firebase.getVetBookingsRef().child(booking.id).setValue(booking).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Firebase.getClientBookings(currentAccount.id).child(booking.id).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(requireActivity(), "You Booked Successfuly", Toast.LENGTH_SHORT).show();
                                        for (int i = 0; i < requireActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                            requireActivity().getSupportFragmentManager().popBackStack();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(requireContext(), "missing fields", Toast.LENGTH_SHORT).show();
            }


        });


    }

    public boolean validate() {
        if (binding.ccn.getText().toString().isEmpty() || binding.cvc.getText().toString().isEmpty() || binding.date.getText().toString().isEmpty()) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        return isEmpty;
    }

}