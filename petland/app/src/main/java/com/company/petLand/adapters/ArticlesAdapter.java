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
import com.company.petLand.databinding.ItemArticleBinding;
import com.company.petLand.databinding.ItemClientOrdersBinding;
import com.company.petLand.fragments.admin.FragmentAdminArticleDetails;
import com.company.petLand.fragments.client.FragmentClientTermsOfService;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Article;
import com.company.petLand.models.Order;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {
    private List<Article> articles = new ArrayList<>();
    boolean canDelete = false;

    Fragment fragment;

    public ArticlesAdapter(List<Article> articles, Fragment fragment, boolean canDelete) {
        this.articles = articles;
        this.fragment = fragment;
        this.canDelete = canDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.binding.name.setText(article.title);
        holder.binding.description.setText(article.description);
        StorageReference ref = Firebase.getArticlesImageRef(article.id);
        Glide.with(holder.binding.image)
                .load(ref)
                .placeholder(R.drawable.ic_pet)
                .error(R.drawable.ic_pet)
                .into(holder.binding.image);
        if (canDelete) {
            holder.binding.remove.setVisibility(View.VISIBLE);
        }else {
            holder.binding.remove.setVisibility(View.GONE);
        }

    }

    public void updateOrders(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemArticleBinding binding;

        public ViewHolder(@NonNull ItemArticleBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
            binding.getRoot().setOnClickListener(view -> {
                        FragmentAdminArticleDetails fragmentAdminArticleDetails = new FragmentAdminArticleDetails();
                        Bundle bundle = new Bundle();
                        Article article = articles.get(getAdapterPosition());
                        bundle.putParcelable("article", article);
                        fragmentAdminArticleDetails.setArguments(bundle);
                        fragment.getParentFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, fragmentAdminArticleDetails, "Article")
                                .addToBackStack("Article")
                                .commit();
                    }
            );

            binding.remove.setOnClickListener(view -> {
                int i = getAdapterPosition();
                Firebase.getArticlesRef().child(articles.get(i).id).removeValue().addOnSuccessListener(unused -> {
                    articles.remove(i);
                    notifyItemRemoved(i);
                });
            });
        }
    }
}
