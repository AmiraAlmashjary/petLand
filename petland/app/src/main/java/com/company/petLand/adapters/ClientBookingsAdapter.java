package com.company.petLand.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.petLand.databinding.ItemClientBookingsBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.models.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ClientBookingsAdapter extends RecyclerView.Adapter<ClientBookingsAdapter.ViewHolder> {
    private List<Booking> bookings = new ArrayList<>();


    public ClientBookingsAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientBookingsBinding binding = ItemClientBookingsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.binding.type.setText(booking.type + " Appointment");
        holder.binding.price.setText(booking.total + " $");
        holder.binding.date.setText(booking.date);
        if (booking.type.equals("Hotel")) {
            holder.binding.serviceTxt.setText("From");
            holder.binding.timeTxt.setText("To");
        }
        holder.binding.service.setText(booking.serviceName);
        holder.binding.time.setText(booking.time);


    }

    public void updateOrders(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemClientBookingsBinding binding;

        public ViewHolder(@NonNull ItemClientBookingsBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
            binding.cancel.setOnClickListener(view -> {
                Booking booking = bookings.get(getAdapterPosition());
                DatabaseReference reference;
                if (booking.type.equals("Vet")) {
                    reference = Firebase.getVetBookingsRef();
                } else if (booking.type.equals("Hotel")) {
                    reference = Firebase.getHotelBookingsRef();
                } else {
                    reference = Firebase.getGroomingBookingsRef();
                }
                        reference
                        .child(booking.id)
                        .removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Firebase.getClientBookings(FirebaseAuth.getInstance().getUid()).child(booking.id).removeValue().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {


                                    } else {
                                        //error
                                    }
                                });

                            } else {
                                //error
                            }
                        });
            });

        }
    }
}
