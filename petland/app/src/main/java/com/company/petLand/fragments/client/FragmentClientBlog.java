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
import com.company.petLand.adapters.ArticlesAdapter;
import com.company.petLand.databinding.FragmentAdminBlogsBinding;
import com.company.petLand.fragments.admin.FragmentAdminAddArticle;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Article;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentClientBlog extends Fragment {
    private FragmentAdminBlogsBinding binding;
    List<Article> articles = new ArrayList<>();
    ArticlesAdapter adapter;

    public FragmentClientBlog() {
    }

    public static FragmentClientBlog newInstance() {
        return new FragmentClientBlog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArticlesAdapter(articles, this, false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminBlogsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);
        Firebase.getArticlesRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    Article article = x.getValue(Article.class);
                    articles.add(article);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.fap.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FragmentAdminAddArticle(), "Add New Article")
                    .addToBackStack("Add New Article")
                    .commit();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}