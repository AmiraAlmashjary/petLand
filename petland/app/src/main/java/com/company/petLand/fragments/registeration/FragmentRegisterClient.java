package com.company.petLand.fragments.registeration;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.custom.ChooseFromGalleryContract;
import com.company.petLand.databinding.FragmentRegisterClientBinding;

import com.company.petLand.fragments.client.ActivityClientMain;
import com.company.petLand.helpers.AuthHelper;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.helpers.Saver;
import com.company.petLand.permissions.MyPermissions;
import com.company.petLand.permissions.PermissionsManager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentRegisterClient extends Fragment {

    private ActivityResultLauncher<String[]> requestCameraPermission;
    private ActivityResultLauncher<Uri> takePicture;
    private ActivityResultLauncher<Integer> pickGallery;
    private FragmentRegisterClientBinding binding;
    private Dialog loadingDialog;

    private Uri imageUri = null;
    private Uri cameraTempUri = null;


    public FragmentRegisterClient() {
    }

    public static FragmentRegisterClient newInstance() {
        return new FragmentRegisterClient();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForActivityResults();
        registerPermissionResults();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterClientBinding.inflate(inflater);
        setTitle("Create Account");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpLoadingDialog();
        setupBackButton();

        binding.registerButton.setOnClickListener(v -> {

            if (validateAll()) {
                showLoadingDialog();
                Firebase.registerClient(
                        getUserName()
                        , getEmail()
                        , getPassword()
                        , imageUri
                        , getPhone()
                        , requireContext()
                        , () -> {
                            hideLoadingDialog();
                            if (Firebase.getAuth().getCurrentUser() != null) {

                                lunchCustomerMainActivity();
                            }
                        });
            }
        });
        binding.image.setOnClickListener(v -> {
            openPictureDialog();
        });

        binding.toolbar.back.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        super.onViewCreated(view, savedInstanceState);
        binding.alreadyHaveAccountTV.setOnClickListener(view2 -> {
            FragmentWelcome fragmentWelcome =  FragmentWelcome.newInstance();
            Bundle bundle =new Bundle();
            bundle.putString("login","login");
            fragmentWelcome.setArguments(bundle);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.container, fragmentWelcome)
                    .commit();

        });
    }

    private Boolean validateAll() {
        return AuthHelper.validateEmail(binding.email, requireActivity())
                && AuthHelper.validateUserName(binding.firstName, requireActivity())
                && AuthHelper.validatePhone(binding.phone, requireContext())
                && AuthHelper.validatePassword(binding.password, binding.password, requireActivity());
    }


    private void setTitle(String text) {
        binding.toolbar.title.setText(text);
    }

    private String getEmail() {
        return binding.email.getText().toString().trim();
    }

    private String getUserName() {
        String name1 = binding.firstName.getText().toString().trim();
        String name2 = binding.lastName.getText().toString().trim();
        return name1 + " " + name2;
    }

    private String getPassword() {
        return binding.password.getText().toString();
    }

    private Long getPhone() {
        return Long.parseLong(binding.phone.getText().toString());
    }


    private void registerForActivityResults() {

        takePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                if (cameraTempUri != null) {
                    imageUri = cameraTempUri;
                    Glide.with(binding.image).load(imageUri).into(binding.image);
                }

            }

        });

        pickGallery = registerForActivityResult(new ChooseFromGalleryContract(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        Glide.with(binding.image).load(imageUri).into(binding.image);

                    }
                });
    }

    private void registerPermissionResults() {
        requestCameraPermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                results -> {
                    boolean isAllGranted = true;
                    for (Boolean result : results.values()) {
                        isAllGranted = isAllGranted && result;
                    }
                    if (isAllGranted) {
                        File file = createFile();
                        cameraTempUri = FileProvider.getUriForFile(requireContext(),
                                "com.company.petLand.provider",
                                file
                        );
                        takePicture.launch(cameraTempUri);
                    }
                });

    }


    public void lunchCustomerMainActivity() {
        Intent intent = new Intent(requireActivity(), ActivityClientMain.class);
        startActivity(intent);
        requireActivity().finish();

    }

    private void openPictureDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_imagepicker);
        Button galleryButton = dialog.findViewById(R.id.Gallery_Button);
        Button cameraButton = dialog.findViewById(R.id.Camera_Button);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        galleryButton.setOnClickListener(view -> {
            pickGallery.launch(0);
            dialog.dismiss();
        });
        cameraButton.setOnClickListener(view -> {
            if (PermissionsManager.isAllPermissionsGranted(requireActivity(), MyPermissions.getCameraPermissions())) {
                File file = createFile();
                cameraTempUri = FileProvider.getUriForFile(requireContext(),
                        "com.company.petLand.provider",
                        file
                );
                takePicture.launch(cameraTempUri);

                dialog.dismiss();
            } else {
                requestCameraPermission.launch(MyPermissions.getCameraPermissions().toArray(new String[]{}));
                dialog.dismiss();
            }


        });
        dialog.show();
    }


    private File createFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            URLEncoder.encode(timeStamp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File storageDir = requireActivity().getCacheDir();
        File file = null;
        try {
            file = File.createTempFile(
                    timeStamp,  /* prefix */
                    ".jpg",  /* suffix */
                    storageDir /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void disableInteractions() {
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void enableInteractions() {
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

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

    private void setupBackButton() {
        binding.toolbar.back.setOnClickListener(v ->
                requireActivity().onBackPressed()
        );
    }

}