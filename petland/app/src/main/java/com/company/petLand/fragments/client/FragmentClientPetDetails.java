package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.databinding.FragmentClientPetDetailsBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Pet;
import com.google.firebase.storage.StorageReference;


public class FragmentClientPetDetails extends Fragment {

    private FragmentClientPetDetailsBinding binding;

    public FragmentClientPetDetails() {
    }

    public static FragmentClientPetDetails newInstance() {
        return new FragmentClientPetDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientPetDetailsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Pet pet = bundle.getParcelable("pet");
            if (pet != null) {
                binding.age.setText(pet.age);
                binding.birth.setText(pet.birth);
                binding.color.setText(pet.color);
                binding.name.setText(pet.name);
                binding.weight.setText(pet.weight);
                binding.species.setText(pet.species);
                StorageReference ref = Firebase.getPetsImageRef(pet.id);
                Glide.with(binding.image)
                        .load(ref)
                        .placeholder(R.drawable.dog_placeholder)
                        .error(R.drawable.dog_placeholder)
                        .into(binding.image);
            }
        }


        ;
    }


}