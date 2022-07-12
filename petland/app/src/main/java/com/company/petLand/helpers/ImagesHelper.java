package com.company.petLand.helpers;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.custom.ChooseFromGalleryContract;
import com.company.petLand.permissions.MyPermissions;
import com.company.petLand.permissions.PermissionsManager;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;


public class ImagesHelper {
    private Uri imageUri = null;
    private Uri cameraTempUri = null;
    private ActivityResultLauncher<String[]> requestCameraPermission;
    private ActivityResultLauncher<Uri> takePicture;
    private ActivityResultLauncher<Integer> pickGallery;
    private Fragment fragment;
    private ImageView imageView;

    public ImagesHelper(Fragment fragment, ImageView imageView) {
        this.fragment = fragment;
        this.imageView = imageView;
        registerResults();
    }

    private void registerResults() {
        registerForActivityResults();
        registerPermissionResults();
    }

    public @Nullable
    Uri getImageUri() {
        return imageUri;
    }

    private void registerForActivityResults() {
        takePicture = fragment.registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                if (cameraTempUri != null) {
                    imageUri = cameraTempUri;
                    File file = getCompressed(imageUri);
                    if (file != null) {
                        imageUri = getFileUri(file);
                    }
                    Glide.with(imageView).load(imageUri).into(imageView);
                }

            }

        });

        pickGallery = fragment.registerForActivityResult(new ChooseFromGalleryContract(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        File file = getCompressed(imageUri);
                        if (file != null) {
                            imageUri = getFileUri(file);
                        }
                        Glide.with(imageView).load(imageUri).into(imageView);

                    }
                });
    }

    private void registerPermissionResults() {
        requestCameraPermission = fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                results -> {
                    boolean isAllGranted = true;
                    for (Boolean result : results.values()) {
                        isAllGranted = isAllGranted && result;
                    }
                    if (isAllGranted) {
                        File file = createFile();
                        cameraTempUri = FileProvider.getUriForFile(fragment.requireContext(),
                                "com.company.petLand.provider",
                                file
                        );
                        takePicture.launch(cameraTempUri);
                    }
                });

    }


    public void openPictureDialog() {
        final Dialog dialog = new Dialog(fragment.requireContext());
        dialog.setContentView(R.layout.dialog_imagepicker);
        Button galleryButton = dialog.findViewById(R.id.Gallery_Button);
        Button cameraButton = dialog.findViewById(R.id.Camera_Button);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        galleryButton.setOnClickListener(view -> {
            pickGallery.launch(0);
            dialog.dismiss();
        });
        cameraButton.setOnClickListener(view -> {
            if (PermissionsManager.isAllPermissionsGranted(fragment.requireActivity(), MyPermissions.getCameraPermissions())) {
                File file = createFile();
                cameraTempUri = FileProvider.getUriForFile(fragment.requireContext(),
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

    public File getCompressed(Uri imageUri) {
        File imgFile = new File(imageUri.getPath());
        File compressedFile = null;
        try {
            compressedFile = new Compressor(fragment.getContext()).compressToFile(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedFile;
    }

    private File createFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            URLEncoder.encode(timeStamp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File storageDir = fragment.requireActivity().getCacheDir();
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

    public Uri getFileUri(File file) {
        Uri uri = FileProvider.getUriForFile(fragment.requireContext(),
                "com.company.petLand.provider",
                file
        );
        return uri;
    }


}
