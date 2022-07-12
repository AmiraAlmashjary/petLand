package com.company.petLand.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.databinding.ItemClientPetsBinding;
import com.company.petLand.fragments.client.FragmentClientPetDetails;
import com.company.petLand.fragments.client.FragmentClientSetAddress;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Pet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.ViewHolder> {

    private List<Pet> pets;
    private Fragment fragment;

    public PetsAdapter(List<Pet> pets, Fragment fragment) {
        this.pets = pets;
        this.fragment = fragment;
    }

    public Pet getItem(int pos) {
        return pets.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientPetsBinding binding = ItemClientPetsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = pets.get(position);
        if (pet != null) {
            holder.binding.name.setText(pet.name);
            holder.binding.age.setText(pet.age + " years");
            StorageReference ref = Firebase.getPetsImageRef(pet.id);
            Glide.with(holder.binding.image)
                    .load(ref)
                    .placeholder(R.drawable.dog_placeholder)
                    .error(R.drawable.dog_placeholder)
                    .into(holder.binding.image);
        }

    }

    public void update(List<Pet> pets) {
        this.pets = pets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemClientPetsBinding binding;

        public ViewHolder(@NonNull ItemClientPetsBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;

//            binding.getRoot().setOnClickListener(v -> {
//                onItemClicked.onClicked(pets.get(getAdapterPosition()).id);
//
//            });
            binding.getRoot().setOnClickListener(view -> {
                FragmentClientPetDetails petDetails = new FragmentClientPetDetails();
                Bundle bundle = new Bundle();
                Pet pet = pets.get(getAdapterPosition());
                bundle.putParcelable("pet",pet);
                petDetails.setArguments(bundle);
                fragment.getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.container,petDetails, "Pet Details")
                        .addToBackStack("Pet Details")
                        .commit();
            });

            binding.remove.setOnClickListener(view -> {

                Pet pet = pets.get(getAdapterPosition());
                Firebase.getAccountPetsRef()
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(pet.id).removeValue()
                        .addOnSuccessListener(unused -> {
                        });
            });
        }

    }

}
