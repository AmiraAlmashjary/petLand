package com.company.petLand.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;

import com.company.petLand.databinding.FragmentClientDetailsBinding;

import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Account;
import com.company.petLand.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class FragmentClientDetails extends Fragment {

    private FragmentClientDetailsBinding binding;
    private Account client;
    public FragmentClientDetails() {
    }

    public static FragmentClientDetails newInstance() {
        return new FragmentClientDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientDetailsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String id = bundle.getString("id");
            Firebase.getAccountRef().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     client = snapshot.getValue(Account.class);
                    if (client != null) {
                        updateUI(client);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        binding.backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });



    }


    private void updateUI(Account family) {
        binding.name.setText(family.name);
        binding.email.setText(family.email);
//        if (client.gender == 1) {
//            binding.gender.setText("Male");
//        } else {
//            binding.gender.setText("Female");
//        }
        binding.phone.setText(family.phone.toString());
        StorageReference ref = Firebase.getProfileImageRef(family.id);
        Glide.with(binding.image).load(ref).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder).into(binding.image);

        binding.button.setText("Chat");

    }
}