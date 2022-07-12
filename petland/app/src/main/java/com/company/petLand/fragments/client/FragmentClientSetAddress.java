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
import com.company.petLand.databinding.FragmentClientSetAddressBinding;

import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Address;


public class FragmentClientSetAddress extends Fragment {
    boolean isEmpty;
    private FragmentClientSetAddressBinding binding;

    public FragmentClientSetAddress() {
    }

    public static FragmentClientSetAddress newInstance() {
        return new FragmentClientSetAddress();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientSetAddressBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        binding.payBtn.setOnClickListener(view -> {
          if(validate()==false) {
              Address address = new Address();
              address.region = binding.region.getText().toString();
              address.district = binding.district.getText().toString();
              address.street = binding.street.getText().toString();
              address.extras = binding.extra.getText().toString();
              address.building = binding.building.getText().toString();
              Firebase.cart.address = address;

              FragmentClientOrderPayment fragment = new FragmentClientOrderPayment();
              Bundle bundle = new Bundle();
              bundle.putString("pay", "order");
              fragment.setArguments(bundle);

              requireActivity().getSupportFragmentManager()
                      .beginTransaction()
                      .add(R.id.container, fragment, "Payment")
                      .addToBackStack("Payment")
                      .commit();
          }else {
              Toast.makeText(requireActivity(), "Missing fields", Toast.LENGTH_SHORT).show();
          }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public boolean validate() {
        if (binding.building.getText().toString().isEmpty() || binding.street.getText().toString().isEmpty() || binding.district.getText().toString().isEmpty()| binding.region.getText().toString().isEmpty()) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        return isEmpty;
    }

}