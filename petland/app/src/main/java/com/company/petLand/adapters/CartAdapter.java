package com.company.petLand.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.databinding.ItemCartBtnBinding;
import com.company.petLand.databinding.ItemCartProductBinding;
import com.company.petLand.databinding.ItemCartTotalBinding;
import com.company.petLand.fragments.client.FragmentClientSetAddress;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Order;
import com.company.petLand.models.Product;
import com.google.firebase.storage.StorageReference;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Order order;
    Fragment fragment;
    private static final int NORMAL_VIEW = 0;
    private static final int TOTAL_VIEW = 1;
    private static final int BTN_VIEW = 2;


    public CartAdapter(Order order, Fragment fragment) {
        this.order = order;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TOTAL_VIEW) {
            ItemCartTotalBinding binding = ItemCartTotalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else if (viewType == BTN_VIEW) {
            ItemCartBtnBinding binding = ItemCartBtnBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else {
            ItemCartProductBinding binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 2 > position) {
            return NORMAL_VIEW;
        } else if (getItemCount() - 1 == position) {
            return BTN_VIEW;
        } else {
            return TOTAL_VIEW;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TOTAL_VIEW) {
            ItemCartTotalBinding totalBinding = (ItemCartTotalBinding) holder.binding;
            double price = 0;
            for (Product x : order.products) {
                price += (x.price * x.amount);
            }
            totalBinding.subtotal.setText(String.valueOf(price) + " $");

            totalBinding.total.setText(String.valueOf(price + 15) + " $");

        }
        if (getItemViewType(position) == BTN_VIEW) {
            ItemCartBtnBinding btnBinding = (ItemCartBtnBinding) holder.binding;

        }

        if (getItemViewType(position) == NORMAL_VIEW) {
            ItemCartProductBinding binding = (ItemCartProductBinding) holder.binding;
            Log.d("TAG", "onBindViewHolder: " + order.products.get(0).id);
            Product product = order.products.get(position);
            if (product != null) {
                binding.name.setText(product.name);
                binding.productprice.setText(String.valueOf(product.price));
                binding.amount.setText("X " + product.amount);
                StorageReference ref = Firebase.getProductsImageRef(product.id);
                Glide.with(binding.image)
                        .load(ref)
                        .placeholder(R.drawable.ic_cart)
                        .error(R.drawable.ic_cart)
                        .into(binding.image);
            }

        }


    }


    @Override
    public int getItemCount() {
        int count = order.products.size() + 2;
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewBinding binding;

        public ViewHolder(@NonNull ViewBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
            if (binding instanceof ItemCartBtnBinding) {
                ItemCartBtnBinding binding = (ItemCartBtnBinding) _binding;
                binding.checkOutBtn.setOnClickListener(view -> {

                    fragment.getParentFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, new FragmentClientSetAddress(), "Set Your Address")
                            .addToBackStack("Set Your Address")
                            .commit();
                });
            }

        }

    }

}
