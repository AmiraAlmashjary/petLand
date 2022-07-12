package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.company.petLand.R;
import com.company.petLand.adapters.PetsAdapter;
import com.company.petLand.databinding.FragmentClientPetsBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentClientPets extends Fragment implements OnItemClicked {
    private FragmentClientPetsBinding binding;
    private List<Pet> pets = new ArrayList<>();
    private PetsAdapter adapter;

    public FragmentClientPets() {
    }


    public static FragmentClientPets newInstance() {
        return new FragmentClientPets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PetsAdapter(pets, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientPetsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Firebase.getAccountPetsRef().child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pets.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    Pet pet = x.getValue(Pet.class);
                    pets.add(pet);
                }
                adapter.notifyDataSetChanged();

                if (pets.isEmpty()) {
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.fap.setOnClickListener(view1 -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentClientAddPets(), "Add Pet")
                    .addToBackStack("addPet")
                    .commit();
        });
    }


    @Override
    public void onClicked(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id); // Put anything what you want
        FragmentClientShop frag = new FragmentClientShop();
        frag.setArguments(bundle);
        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.container, frag, null)
                .addToBackStack("petDetails")
                .commit();
    }
}