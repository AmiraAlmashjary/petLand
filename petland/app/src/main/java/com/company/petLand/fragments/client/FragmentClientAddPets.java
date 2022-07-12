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
import com.company.petLand.databinding.FragmentClientNewPetBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.helpers.ImagesHelper;
import com.company.petLand.helpers.LoadingDialog;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Pet;
import com.google.firebase.auth.FirebaseAuth;


public class FragmentClientAddPets extends Fragment implements OnItemClicked {
    private FragmentClientNewPetBinding binding;
    private ImagesHelper imagesHelper;

    public FragmentClientAddPets() {
    }

    public static FragmentClientAddPets newInstance() {
        return new FragmentClientAddPets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientNewPetBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        imagesHelper = new ImagesHelper(this, binding.image);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.image.setOnClickListener(view1 -> {
            imagesHelper.openPictureDialog();
        });
        binding.addPet.setOnClickListener(view1 -> {
            Pet pet = new Pet();
            pet.name = binding.name.getText().toString();
            pet.age = binding.age.getText().toString();
            pet.weight = binding.weight.getText().toString();
            pet.species = binding.species.getText().toString();
            pet.color = binding.color.getText().toString();
            pet.birth = binding.birth.getText().toString();
            if(imagesHelper.getImageUri()== null){
                Toast.makeText(requireContext(), "Please Put an Image", Toast.LENGTH_SHORT).show();
            }else {
                LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
                loadingDialog.showLoadingDialog();
                Firebase.addNewPet(FirebaseAuth.getInstance().getUid(), pet, imagesHelper.getImageUri(),requireContext(), () -> {
                 loadingDialog.hideLoadingDialog();
                    requireActivity().onBackPressed();
                });
            }

        });
    }


    @Override
    public void onClicked(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id); // Put anything what you want
        FragmentClientShop frag = new FragmentClientShop();
        frag.setArguments(bundle);
        FragmentClientAddPets.this
                .getParentFragment()
                .getParentFragmentManager()
                .beginTransaction()
                .add(R.id.container, frag, null)
                .addToBackStack("products")
                .commit();
    }


}