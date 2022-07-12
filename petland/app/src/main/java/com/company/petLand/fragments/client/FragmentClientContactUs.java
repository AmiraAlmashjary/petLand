package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.databinding.FragmentClientContactusBinding;


public class FragmentClientContactUs extends Fragment {

    private FragmentClientContactusBinding binding;


    public FragmentClientContactUs() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClientContactusBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return binding.getRoot();
    }


}