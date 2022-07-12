package com.company.petLand.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.company.petLand.R;
import com.company.petLand.adapters.AdminOrdersAdapter;
import com.company.petLand.adapters.ClientOrdersAdapter;
import com.company.petLand.databinding.FragmentClientOrdersBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnItemClicked;
import com.company.petLand.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdminOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdminOrders extends Fragment {
    FragmentClientOrdersBinding binding;
    AdminOrdersAdapter adapter;
    List<Order> orders = new ArrayList<>();
//    OnItemClicked onItemClicked = id -> {
//        FragmentClientDetails clientDetails = new FragmentClientDetails();
//        Bundle bundle = new Bundle();
//        bundle.putString("id", id);
//        clientDetails.setArguments(bundle);
//        getParentFragment().getParentFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, clientDetails)
//                .addToBackStack(null)
//                .commit();
//    };

    public FragmentAdminOrders() {
    }


    public static FragmentAdminOrders newInstance() {
        return new FragmentAdminOrders();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        adapter = new AdminOrdersAdapter(orders);


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClientOrdersBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter);
        Firebase.getAdminOrdersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot x : snapshot.getChildren()) {
                    Order order = x.getValue(Order.class);
                    orders.add(order);
                }
                adapter.notifyDataSetChanged();
                if(orders.isEmpty()){
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                }else {
                    binding.emptyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider));
//        binding.recycler.addItemDecoration(divider);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


//    private void getOrders() {
//        orders.clear();
//        Firebase.getClientOrdersRef().child(Firebase.getAuth().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot x : snapshot.getChildren()) {
//                            Order order = x.getValue(Order.class);
//                            orders.add(order);
//                        }
//                        adapter.updateOrders(orders);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//
//                    }
//                });
    }


//    @Override
//    public void onClicked(int pos) {
//        Bundle bundle = new Bundle();
//        Family family = adapter.getItem(pos);
//        bundle.putString("id", family.id); // Put anything what you want
//        FragmentFamilyDetails frag = new FragmentFamilyDetails();
//        frag.setArguments(bundle);
//        FragmentClientOrders.this
//                .getParentFragment()
//                .getParentFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, frag, null)
//                .addToBackStack("details")
//                .commit();
//    }
