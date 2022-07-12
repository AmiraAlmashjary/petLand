package com.company.petLand.fragments.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;

import com.company.petLand.databinding.FragmentFamilyProfileBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.helpers.ImagesHelper;

import com.company.petLand.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class FragmentAdminProfile extends Fragment {
    private FragmentFamilyProfileBinding binding;
    private Account tempFamily;
    boolean edit = false;
    ImagesHelper helper;

    public FragmentAdminProfile() {
    }
    public static FragmentAdminProfile newInstance() {
        return new FragmentAdminProfile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFamilyProfileBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        loadUser();
        helper = new ImagesHelper(this, binding.image);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupEditing();

    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Account family) {
        binding.name.setText(family.name);
        binding.email.setText(family.email);

        binding.description.setText(family.description);
        binding.phone.setText(family.phone.toString());
        StorageReference ref = Firebase.getProfileImageRef(Firebase.getAuth().getUid());
        Glide.with(binding.image)
                .load(ref)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(binding.image);
    }

    private void loadUser() {
        Firebase.getAccountRef().child(Firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempFamily = snapshot.getValue(Account.class);
                if (tempFamily != null) {
                    updateUI(tempFamily);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupEditing() {
        binding.edit.setOnClickListener(v -> {
            if (binding.edit.getText().equals("Edit")) {
                edit = true;
                for (int i = 0; i < binding.linearLayout2.getChildCount(); i++) {
                    if (i == 0 || i == 2 || i == 4) {
                        LinearLayout ll = (LinearLayout) binding.linearLayout2.getChildAt(i);
                        TextView textView = (TextView) ll.getChildAt(1);
                        enableEdit(textView);
                    }
                }
                binding.image.setOnClickListener(view -> {

                    helper.openPictureDialog();
                });
                binding.edit.setText("Save");
            } else if (binding.edit.getText().equals("Save")) {
                edit = false;
                tempFamily.description = binding.description.getText().toString();
                tempFamily.name = binding.name.getText().toString();
                tempFamily.description = binding.description.getText().toString();
                tempFamily.phone = Long.valueOf(binding.phone.getText().toString());
                if (helper.getImageUri() != null)
                    Firebase.getProfileImageRef(Firebase.getAuth().getUid()).putFile(helper.getImageUri());
                Firebase.getAccountRef().child(Firebase.getAuth().getUid()).setValue(tempFamily).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext().getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < binding.linearLayout2.getChildCount(); i++) {
                            if (i == 0 || i == 2 || i == 4) {
                                LinearLayout ll = (LinearLayout) binding.linearLayout2.getChildAt(i);
                                TextView textView = (TextView) ll.getChildAt(1);
                                disableEdit(textView);
                            }
                            binding.image.setOnClickListener(null);
                            binding.edit.setText("Edit");
                        }
                    }
                });
            }
        });
    }


    private void enableEdit(TextView tv) {
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setFocusableInTouchMode(true);
    }

    private void disableEdit(TextView tv) {
        tv.setFocusable(false);
        tv.setClickable(false);
        tv.setFocusableInTouchMode(false);
    }
}