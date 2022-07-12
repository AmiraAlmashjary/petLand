package com.company.petLand.fragments.admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.company.petLand.R;
import com.company.petLand.adapters.ProductsAdapter;
import com.company.petLand.custom.ChooseFromGalleryContract;

import com.company.petLand.databinding.FragmentAdminShopBinding;
import com.company.petLand.helpers.Firebase;
import com.company.petLand.interfaces.OnCompletionListener;
import com.company.petLand.models.Product;
import com.company.petLand.permissions.MyPermissions;
import com.company.petLand.permissions.PermissionsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentAdminShop extends Fragment {
    private ActivityResultLauncher<String[]> requestCameraPermission;
    private ActivityResultLauncher<Uri> takePicture;
    private ActivityResultLauncher<Integer> pickGallery;
    private FragmentAdminShopBinding binding;
    private List<Product> products = new ArrayList<>();
    private Uri imageUri, cameraTempUri;
    private Dialog productDialog;

    public FragmentAdminShop() {
    }


    public static FragmentAdminShop newInstance() {
        return new FragmentAdminShop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForActivityResults();
        registerPermissionResults();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminShopBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        RecyclerView.LayoutManager manager = new GridLayoutManager(requireContext(), 2);
        binding.recycler.setLayoutManager(manager);
        setUpAddFap();
        setUpAddDialog();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProducts();

    }


    private void loadProducts() {
        products = new ArrayList<>();
        Firebase.getProductsRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        products.clear();
                        for (DataSnapshot x : snapshot.getChildren()) {
                            Product product = x.getValue(Product.class);
                            products.add(product);
                        }
                        ProductsAdapter adapter = new ProductsAdapter(products, FragmentAdminShop.this,true);
                        binding.recycler.setAdapter(adapter);

                        if(products.isEmpty()){
                            binding.emptyLayout.setVisibility(View.VISIBLE);
                        }else {
                            binding.emptyLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void setUpAddFap() {
        binding.fap.setOnClickListener(v -> {
            productDialog.show();
        });
    }


    private void setUpAddDialog() {
        productDialog = new Dialog(requireContext());
        productDialog.setContentView(R.layout.dialog_add_product);
        ImageView image = productDialog.findViewById(R.id.image);
        EditText name = productDialog.findViewById(R.id.name);
        EditText price = productDialog.findViewById(R.id.price);
        Button button = productDialog.findViewById(R.id.save);

        productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        image.setOnClickListener(v -> openPictureDialog());
        button.setOnClickListener(view -> {
            boolean a = false, b = false, c = false;
            if (imageUri == null) {
                Toast.makeText(requireContext(), "There is no image ", Toast.LENGTH_SHORT).show();
            } else {
                a = true;
            }
            if (name.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
            } else {
                b = true;
            }

            if (price.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Price is Empty", Toast.LENGTH_SHORT).show();
            } else {
                c = true;
            }


            if (a & b & c) {
                Product product = new Product(name.getText().toString(), "",Double.parseDouble(price.getText().toString()));
                saveProduct(product, () -> {
                    productDialog.dismiss();
                    imageUri = null;
                    loadProducts();
                });
            }
        });

    }


    private void saveProduct(Product product, OnCompletionListener listener) {
        String key = Firebase.getProductsRef().push().getKey();
        product.id = key;
        Firebase.getProductsRef()
                .child(key)
                .setValue(product)
                .addOnSuccessListener(task -> Firebase.uploadProductImage(imageUri, product.id, listener));

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



    // Registering Reuslts
    private void registerForActivityResults() {
        takePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                if (cameraTempUri != null) {
                    imageUri = cameraTempUri;
                    ImageView imageView = productDialog.findViewById(R.id.image);
                    Glide.with(imageView).load(imageUri).into(imageView);
                }

            }

        });

        pickGallery = registerForActivityResult(new ChooseFromGalleryContract(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        ImageView imageView = productDialog.findViewById(R.id.image);
                        Glide.with(imageView).load(imageUri).into(imageView);

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
}