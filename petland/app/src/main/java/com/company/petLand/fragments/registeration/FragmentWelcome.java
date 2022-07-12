package com.company.petLand.fragments.registeration;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.databinding.FragmentWelcomeBinding;

import com.company.petLand.fragments.admin.ActivityAdminMain;
import com.company.petLand.fragments.client.ActivityClientMain;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnBackClickedLinstener;
import com.company.petLand.models.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class FragmentWelcome extends Fragment implements OnBackClickedLinstener {
    private Dialog loadingDialog;
    private Boolean isDefault = true;

    private FragmentWelcomeBinding binding;

    public FragmentWelcome() {

    }


    public static FragmentWelcome newInstance() {
        return new FragmentWelcome();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater);
        setUpLoadingDialog();
        if (getArguments() != null) {
            showLogin();
        }
        binding.loginLayout.loginButton.setOnClickListener(v -> {
            String email = binding.loginLayout.EmailET.getText().toString().trim();
            String password = binding.loginLayout.PasswordET.getText().toString().trim();
            Firebase.login(email, password, isLogged -> {
                if (isLogged) {
                    hideLoadingDialog();
                    checkTypeAndRedirect();
                } else {
                    Toast.makeText(requireContext(), "Bad Credentials", Toast.LENGTH_SHORT).show();
                    hideLoadingDialog();
                }
            });


        });

        binding.loginButton.setOnClickListener(v -> {
            showLogin();
        });

        binding.registerButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.container, FragmentRegisterClient.newInstance())
                    .addToBackStack(null)
                    .commit();
            isDefault = true;
        });

        binding.loginLayout.dontHaveAccountBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.container, FragmentRegisterClient.newInstance())
                    .addToBackStack(null)
                    .commit();
            isDefault = true;
        });

        binding.registerLayout.customerButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.container, FragmentRegisterClient.newInstance())
                    .addToBackStack(null)
                    .commit();
            isDefault = true;

        });

        binding.registerLayout.providerButton.setOnClickListener(v -> {
//            requireActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .setReorderingAllowed(true)
//                    .setCustomAnimations(
//                            R.anim.slide_in,  // enter
//                            R.anim.fade_out,  // exit
//                            R.anim.fade_in,   // popEnter
//                            R.anim.slide_out  // popExit
//                    )
//                    .replace(R.id.container, FragmentRegisterFamily.newInstance())
//                    .addToBackStack(null)
//                    .commit();
//            isDefault = true;

        });
        return binding.getRoot();
    }

    private void checkTypeAndRedirect() {

        Firebase.getAccountRef().child(Firebase.getAuth().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Account account = snapshot.getValue(Account.class);
                    if (account.type.equals("customer")) {
                        redirectCustomerHome();
                    } else {
                        lunchAdminMain();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lunchAdminMain() {
        startActivity(new Intent(requireActivity(), ActivityAdminMain.class));
        requireActivity().finish();
    }

    private void redirectCustomerHome() {
        startActivity(new Intent(requireActivity(), ActivityClientMain.class));
        requireActivity().finish();
        ;
    }

    private void showWelcome() {
        binding.loginLayout.getRoot().setVisibility(View.GONE);
        binding.registerLayout.getRoot().setVisibility(View.GONE);
        binding.loginButton.setVisibility(View.VISIBLE);
        binding.registerButton.setVisibility(View.VISIBLE);
        isDefault = true;
    }

    private void showLogin() {
        binding.loginButton.setVisibility(View.GONE);
        binding.registerButton.setVisibility(View.GONE);
        binding.loginLayout.getRoot().setVisibility(View.VISIBLE);
        isDefault = false;
    }

    private void showRegister() {
        binding.loginButton.setVisibility(View.GONE);
        binding.registerButton.setVisibility(View.GONE);
        binding.registerLayout.getRoot().setVisibility(View.VISIBLE);
        isDefault = false;
    }

//    private void hideLoginShowRegister() {
//        binding.loginLayout.getRoot().setVisibility(View.GONE);
//        binding.registerLayout.getRoot().setVisibility(View.VISIBLE);
//        isDefault = false;
//    }

    private void setUpLoadingDialog() {
        loadingDialog = new Dialog(requireContext());
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void showLoadingDialog() {
        loadingDialog.show();
        disableInteractions();


    }

    private void hideLoadingDialog() {
        loadingDialog.hide();
        enableInteractions();

    }

    private void disableInteractions() {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void enableInteractions() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public Boolean onBackClicked() {
        if (!isDefault) {
            showWelcome();
            return false;
        } else {
            return true;
        }
    }


}