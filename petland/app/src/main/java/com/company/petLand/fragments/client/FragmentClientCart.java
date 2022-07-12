package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.adapters.CartAdapter;
import com.company.petLand.databinding.FragmentClientCartBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Product;


public class FragmentClientCart extends Fragment {

    private FragmentClientCartBinding binding;
    private CartAdapter cartAdapter;

    public FragmentClientCart() {
    }

    public static FragmentClientCart newInstance() {
        return new FragmentClientCart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientCartBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        cartAdapter = new CartAdapter(Firebase.cart, this);
        binding.recycler.setAdapter(cartAdapter);
        double price = 0;
        for (Product x : Firebase.cart.products) {
            price += (x.price * x.amount);
        }
        Firebase.cart.total = price + 15;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}


//    private void loadProduct(Product product) {
//        binding.name.setText(product.name);
//        binding.description.setText(product.description);
//        binding.price.setText(String.valueOf(product.price) + " $");
//        if (Firebase.cart != null) {
//            Product productTemp = Firebase.cart.get(product);
//            if (productTemp != null) {
//                binding.amount.setText(productTemp.amount);
//                currentProduct = productTemp;
//            }
//        }
//        StorageReference ref = Firebase.getProductsImageRef(product.id);
//        Glide.with(binding.image)
//                .load(ref)
//                .placeholder(R.drawable.ic_cart)
//                .error(R.drawable.ic_cart)
//                .into(binding.image);
//
//
//    }
