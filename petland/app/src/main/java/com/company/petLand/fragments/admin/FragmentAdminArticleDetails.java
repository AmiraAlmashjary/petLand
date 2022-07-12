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
import com.company.petLand.databinding.FragmentAdminBlogsBinding;
import com.company.petLand.databinding.FragmentArticleDetailsBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.helpers.ImagesHelper;
import com.company.petLand.models.Account;
import com.company.petLand.models.Article;
import com.google.firebase.storage.StorageReference;


public class FragmentAdminArticleDetails extends Fragment {
    private FragmentArticleDetailsBinding binding;


    public FragmentAdminArticleDetails() {
    }

    public static FragmentAdminArticleDetails newInstance() {
        return new FragmentAdminArticleDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArticleDetailsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Article article = bundle.getParcelable("article");
            if (article != null) {
                binding.title.setText(article.title);
                binding.description.setText(article.description);
                StorageReference ref = Firebase.getArticlesImageRef(article.id);
                Glide.with(binding.image)
                        .load(ref)
                        .placeholder(R.drawable.ic_pet)
                        .error(R.drawable.ic_pet)
                        .into(binding.image);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}