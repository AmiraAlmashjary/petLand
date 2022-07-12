package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.databinding.FragmentProductDetailsBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Order;
import com.company.petLand.models.Product;
import com.google.firebase.storage.StorageReference;


public class FragmentProductDetails extends Fragment {

    private FragmentProductDetailsBinding binding;
    Product currentProduct = null;

    public FragmentProductDetails() {
    }

    public static FragmentProductDetails newInstance() {
        return new FragmentProductDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentProduct = bundle.getParcelable("product");
            if (currentProduct != null) {
                loadProduct(currentProduct);
            }

        }

        binding.button.setOnClickListener(view1 -> {
            if (Firebase.cart != null) {
                Firebase.cart.addProduct(currentProduct);
                requireActivity().onBackPressed();
            } else {
                Firebase.cart = new Order();
                Firebase.cart.addProduct(currentProduct);
                requireActivity().onBackPressed();
            }
        });
        binding.add.setOnClickListener(view1 -> {
            int i = Integer.valueOf(binding.count.getText().toString());
            binding.count.setText(String.valueOf(++i));
            currentProduct.amount = i;
        });
        binding.minus.setOnClickListener(view1 -> {
            int i = Integer.valueOf(binding.count.getText().toString());
            if (i > 1) {
                binding.count.setText(String.valueOf(--i));
                currentProduct.amount = i;
            }

        });
    }


    private void loadProduct(Product product) {
        binding.name.setText(product.name);
        binding.description.setText(product.description);
        binding.price.setText(String.valueOf(product.price) + " $");
        if (Firebase.cart != null) {
            Product productTemp = Firebase.cart.get(product);
            if (productTemp != null) {
                binding.count.setText(String.valueOf(productTemp.amount));
                currentProduct = productTemp;
            }
        }
        StorageReference ref = Firebase.getProductsImageRef(product.id);
        Glide.with(binding.image)
                .load(ref)
                .placeholder(R.drawable.ic_cart)
                .error(R.drawable.ic_cart)
                .into(binding.image);


    }
}