package com.company.petLand.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.databinding.FragmentAdminAddArticleBinding;

import com.company.petLand.fragments.client.FragmentClientShop;

import com.company.petLand.helpers.Firebase;
import com.company.petLand.helpers.ImagesHelper;
import com.company.petLand.helpers.LoadingDialog;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Article;


public class FragmentAdminAddArticle extends Fragment implements OnItemClicked {
    private FragmentAdminAddArticleBinding binding;
    private ImagesHelper imagesHelper;

    public FragmentAdminAddArticle() {
    }

    public static FragmentAdminAddArticle newInstance() {
        return new FragmentAdminAddArticle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminAddArticleBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        imagesHelper = new ImagesHelper(this, binding.image);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.image.setOnClickListener(view1 -> {
            imagesHelper.openPictureDialog();
        });
        binding.addArticle.setOnClickListener(view1 -> {
            Article article = new Article();
            article.title = binding.title.getText().toString();
            article.description = binding.description.getText().toString();

            if (imagesHelper.getImageUri() == null) {
                Toast.makeText(requireContext(), "Please Put an Image", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog loadingDialog = new LoadingDialog(requireActivity());
                loadingDialog.showLoadingDialog();
                Firebase.addNewArticle(article, imagesHelper.getImageUri(), requireContext(), () -> {
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
        FragmentAdminAddArticle.this
                .getParentFragment()
                .getParentFragmentManager()
                .beginTransaction()
                .add(R.id.container, frag, null)
                .addToBackStack("products")
                .commit();
    }


}