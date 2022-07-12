package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.petLand.adapters.ProductsAdapter;

import com.company.petLand.databinding.FragmentPetShopBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentClientShop extends Fragment {

    private FragmentPetShopBinding binding;
    private List<Product> products = new ArrayList<>();
    private ProductsAdapter adapter;

    public FragmentClientShop() {
    }

    public static FragmentClientShop newInstance() {
        return new FragmentClientShop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPetShopBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        RecyclerView.LayoutManager manager = new GridLayoutManager(requireContext(), 2);
        binding.recycler.setLayoutManager(manager);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProducts();
    }

    private void loadProducts() {
        products.clear();
        Firebase.getProductsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot x : snapshot.getChildren()) {
                    Product product = x.getValue(Product.class);
                    products.add(product);
                }
                adapter = new ProductsAdapter(products, FragmentClientShop.this,false);
                binding.recycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//


//    private void openFamilyProfile() {
//        Bundle bundle = new Bundle();
//
//        FragmentPetDetails frag = FragmentPetDetails.newInstance();
//        frag.setArguments(bundle);
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, frag)
//                .addToBackStack("detailes")
//                .commit();
//    }

//    private OnProductAddedListener getProductAddingListener() {
//
//        return (Product product) -> {
//            if(Firebase.cart==null){
//                Firebase.cart = new Order(tempClient, product);
//            }else {
//                Firebase.cart.addProduct(product);
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
//            builder.setTitle("Confirm")
//                    .setMessage("Are You sure ?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//
//                        String key = Firebase.getClientOrdersRef().child(Firebase.getCurrentUser().getUid()).push().getKey();
//                        Firebase.getClientOrdersRef()
//                                .child(Firebase.getCurrentUser().getUid())
//                                .child(key)
//                                .setValue(Firebase.cart);
//                        Firebase.getAdminOrdersRef()
//                                .child(key)
//                                .setValue(Firebase.cart);
//
//
//                        new AlertDialog.Builder(requireContext())
//                                .setTitle("Order is being Processed")
//                                .setMessage("The order have been requested thanks")
//                                .setPositiveButton(android.R.string.yes, (dialog1, which1) -> {
//                                    requireActivity().onBackPressed();
//                                }).show();
//
//
//                    }).setNegativeButton("No", (dialog, which) -> {
//
//            }).show();
//        };
//    };};


    private String getFamilyOrderId(String productId, String clientId) {

        int result = productId.compareTo(clientId);
        if (result < 0) {
            return productId + clientId;
        } else {
            return clientId + productId;
        }
    }

    private String getClientOrderId(String productId, String familyId) {

        int result = productId.compareTo(familyId);
        if (result < 0) {
            return productId + familyId;
        } else {
            return familyId + productId;
        }
    }

}