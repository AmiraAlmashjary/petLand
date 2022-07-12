package com.company.petLand.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.petLand.databinding.ItemAdminOrdersBinding;
import com.company.petLand.databinding.ItemClientOrdersBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder> {
    private List<Order> clientsOrders = new ArrayList<>();

    public AdminOrdersAdapter(List<Order> clientOrders) {
        this.clientsOrders = clientOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrdersBinding binding = ItemAdminOrdersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = clientsOrders.get(position);
        if (order.client!=null)holder.binding.username.setText(order.client.name);
        holder.binding.orderId.setText(order.id);
        holder.binding.price.setText(order.total+" $");
        holder.binding.location.setText(order.address.region+","+order.address.district);
        holder.binding.status.setText(order.status);
    }

    public void  updateOrders(List<Order> orders){
        this.clientsOrders = orders;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return clientsOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemAdminOrdersBinding binding;

        public ViewHolder(@NonNull ItemAdminOrdersBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
            binding.getRoot().setOnClickListener(view ->{});
            binding.finish.setOnClickListener(view -> {
                Order order = clientsOrders.get(getAdapterPosition());
                Firebase.getClientOrdersRef()
                        .child(Firebase.getCurrentUser().getUid())
                        .child(order.id)
                        .removeValue().addOnSuccessListener(unused ->
                        Firebase.getAdminOrdersRef()
                                .child(order.id)
                                .removeValue().addOnSuccessListener(unused1 -> {
//                            int index = clientsOrders.indexOf(order);
//                            clientsOrders.remove(index);
//                            notifyItemRemoved(index);

                        }));
            });
        }
    }
}
