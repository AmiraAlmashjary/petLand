package com.company.petLand.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.petLand.databinding.ItemClientOrdersBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Order;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ClientOrdersAdapter extends RecyclerView.Adapter<ClientOrdersAdapter.ViewHolder> {
    private List<Order> clientsOrders = new ArrayList<>();
    OnItemClicked listener;

    public ClientOrdersAdapter(List<Order> clientOrders) {
        this.clientsOrders = clientOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientOrdersBinding binding = ItemClientOrdersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    public void setListener(OnItemClicked listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = clientsOrders.get(position);
        holder.binding.orderId.setText(order.id);
        holder.binding.price.setText(order.total + " $");
        holder.binding.status.setText(order.status);
    }

    public void updateOrders(List<Order> orders) {
        this.clientsOrders = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return clientsOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemClientOrdersBinding binding;

        public ViewHolder(@NonNull ItemClientOrdersBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
            binding.getRoot().setOnClickListener(view -> listener.onClicked(clientsOrders.get(getAdapterPosition()).id));

            binding.cancel.setOnClickListener(view -> {
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
