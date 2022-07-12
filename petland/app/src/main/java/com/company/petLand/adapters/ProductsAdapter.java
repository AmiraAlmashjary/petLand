package com.company.petLand.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.databinding.ItemProductBinding;
import com.company.petLand.fragments.client.FragmentProductDetails;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnProductAddedListener;
import com.company.petLand.models.Product;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<Product> products = new ArrayList<>();
    private Boolean isAdmin = false;
    private Fragment fragment;

    public ProductsAdapter(List<Product> products, Fragment fragment, Boolean isAdmin) {
        this.products = products;
        this.fragment = fragment;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.binding.name.setText(product.name);
        holder.binding.price.setText(product.price + " SR");

        StorageReference ref = Firebase.getProductsImageRef(product.id);
        if (isAdmin) {
            holder.binding.orderBtn.setText("Delete");
        } else {
            holder.binding.orderBtn.setText("Add To Cart");
        }

        Glide.with(holder.binding.image)
                .load(ref)
                .placeholder(R.drawable.ic_cart)
                .error(R.drawable.ic_cart)
                .into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;

            binding.orderBtn.setOnClickListener(v -> {
                Product product = products.get(getAdapterPosition());
                if (binding.orderBtn.getText().equals("Delete")) {
                    Firebase.getProductsRef()
                            .child(product.id).removeValue()
                            .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Firebase.getProductsImageRef(product.id).delete();
                        }
                    });
                } else {
                    FragmentProductDetails fragmentProductDetails = new FragmentProductDetails();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("product", product);
                    fragmentProductDetails.setArguments(bundle);
                    fragment.getParentFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragmentProductDetails, "Product Details")
                            .addToBackStack("Product Details")
                            .commit();
                }




            });
        }


    }
}
