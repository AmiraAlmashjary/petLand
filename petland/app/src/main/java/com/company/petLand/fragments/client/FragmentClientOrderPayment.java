package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.databinding.FragmentClientPaymentBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FragmentClientOrderPayment extends Fragment {

    private FragmentClientPaymentBinding binding;
    private Account currentAccount;
    boolean isEmpty;

    public FragmentClientOrderPayment() {
    }

    public static FragmentClientOrderPayment newInstance() {
        return new FragmentClientOrderPayment();
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
        Bundle  bundle =getArguments();
        String type= bundle.getString("pay");

            binding.price.setText("You will be charged" + " " + Firebase.cart.total + "$");
            binding.confirm.setOnClickListener(view1 -> {
                Firebase.cart.client = currentAccount;
                String key = Firebase.getClientOrdersRef().child(Firebase.getCurrentUser().getUid()).push().getKey();
                Firebase.cart.status = "Processing";
                Firebase.cart.id = key;

                if(validate()==false){
                    Firebase.getClientOrdersRef().child(Firebase.getCurrentUser().getUid())
                            .child(key)
                            .setValue(Firebase.cart).addOnSuccessListener(unused ->
                            Firebase.getAdminOrdersRef()
                                    .child(key)
                                    .setValue(Firebase.cart).addOnSuccessListener(unused1 -> {
                                Toast.makeText(requireActivity(), "Your Order is On The Way ,thank", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < requireActivity().getSupportFragmentManager().getBackStackEntryCount(); i++) {
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                }
                            }));
                }else {
                    Toast.makeText(requireContext(), "missing fields", Toast.LENGTH_SHORT).show();
                }


            });



    }

public boolean validate(){
        if(binding.ccn.getText().toString().isEmpty()||binding.cvc.getText().toString().isEmpty()||binding.date.getText().toString().isEmpty()){
            isEmpty = true;
        }else {
            isEmpty = false;
        }
        return isEmpty;
}

}