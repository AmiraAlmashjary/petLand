package com.company.petLand.helpers;

import static com.company.petLand.helpers.Constants.ACCOUNTS;
import static com.company.petLand.helpers.Constants.ADMIN_ORDERS;
import static com.company.petLand.helpers.Constants.ARTICLES;
import static com.company.petLand.helpers.Constants.BOOKINGS;
import static com.company.petLand.helpers.Constants.CLIENT_BOOKINGS;
import static com.company.petLand.helpers.Constants.CLIENT_ORDERS;
import static com.company.petLand.helpers.Constants.CLIENT_PETS;
import static com.company.petLand.helpers.Constants.PRODUCTS;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.petLand.interfaces.OnCompletionListener;
import com.company.petLand.interfaces.OnLoginListener;
import com.company.petLand.models.Account;
import com.company.petLand.models.Article;
import com.company.petLand.models.Order;
import com.company.petLand.models.Pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Firebase {
    public static final String TAG = "Firebase";
    private static final StorageReference IMAGE_REF = getStorage().getReference().child("images/");

    public static FirebaseStorage getStorage() {
        return FirebaseStorage.getInstance();
    }

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static Order cart ;

    public static DatabaseReference getAccountRef() {
        return database.getReference(ACCOUNTS);
    }

    public static DatabaseReference getArticlesRef() {
        return database.getReference(ARTICLES);
    }
    public static DatabaseReference getVetBookingsRef() {
        return database.getReference(BOOKINGS).child("vet");
    }
    public static DatabaseReference getGroomingBookingsRef() {
        return database.getReference(BOOKINGS).child("grooming");
    }
    public static DatabaseReference getHotelBookingsRef() {
        return database.getReference(BOOKINGS).child("hotel");
    }

    public static DatabaseReference getClientOrdersRef() {
        return database.getReference(CLIENT_ORDERS);
    }
    public static DatabaseReference getAdminOrdersRef() {
        return database.getReference(ADMIN_ORDERS);
    }

    public static DatabaseReference getProductsRef() {
        return database.getReference(PRODUCTS);
    }

    public static DatabaseReference getAccountPetsRef() {
        return database.getReference(CLIENT_PETS);
    }

    public static DatabaseReference getClientBookings(String clientId) {
        return database.getReference(CLIENT_BOOKINGS).child(clientId);
    }
    @Nullable
    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static StorageReference getProfileImageRef(String userId) {
        return getStorage().getReference().child("users").child(userId).child("image.png");
    }

    public static StorageReference getPetsImageRef(String petId) {
        return getStorage().getReference().child("pets").child(petId).child("image.png");
    }
    public static StorageReference getArticlesImageRef(String articleId) {
        return getStorage().getReference().child("articles").child(articleId).child("image.png");
    }
    public static StorageReference getProductsImageRef(String productId) {
        return getStorage().getReference().child("products").child(productId).child("image.png");
    }

    public static void registerClient(String name, String email, String password, Uri imageUri, Long phone, Context context, OnCompletionListener registerListener) {
        getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser fireUser = getAuth().getCurrentUser();
                UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                builder.setDisplayName(name);
                fireUser.updateProfile(builder.build());
                Account account = new Account(fireUser.getUid(), name, fireUser.getEmail(), 0l, "customer", "Available");
                account.setPhone(phone);
                getAccountRef().child(getAuth().getUid()).setValue(account).addOnSuccessListener(
                        aVoid -> uploadProfileImage(imageUri, registerListener));
            } else {
                if (task.getException() != null && task.getException().getMessage() != null) {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                registerListener.onCompletion();
            }
        });

    }

    public static void registerAdmin(String name, String email, String password, Uri imageUri, Long phone, Context context, OnCompletionListener registerListener) {
        getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fireUser = getAuth().getCurrentUser();
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setDisplayName(name);
                        fireUser.updateProfile(builder.build());
                        Account user = new Account(fireUser.getUid(), name, fireUser.getEmail(), 1l, "family", "Available");
                        user.setPhone(phone);

                        getAccountRef().child(getAuth().getUid()).setValue(user).addOnSuccessListener(
                                aVoid -> uploadProfileImage(imageUri, registerListener));
                    } else {
                        if (task.getException() != null && task.getException().getMessage() != null) {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                        registerListener.onCompletion();
                    }

                });

    }

    public static void addNewPet(String userId, Pet pet, Uri imageUri, Context context, OnCompletionListener registerListener) {
        String key = getAccountPetsRef().child(userId).push().getKey();
        pet.id = key;
        if (imageUri != null) {
            getPetsImageRef(key).putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    getAccountPetsRef().child(userId).child(key).setValue(pet).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            registerListener.onCompletion();
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            registerListener.onCompletion();
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    registerListener.onCompletion();
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public static void addNewArticle(Article article, Uri imageUri, Context context, OnCompletionListener registerListener) {
        String key = getArticlesRef().push().getKey();
        article.id = key;
        if (imageUri != null) {
            getArticlesImageRef(key).putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    getArticlesRef().child(key).setValue(article).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            registerListener.onCompletion();
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            registerListener.onCompletion();
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    registerListener.onCompletion();
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public static void uploadProfileImage(Uri imageUri, OnCompletionListener listener) {
        if (imageUri != null) {

            UploadTask uploadTask = getProfileImageRef(getAuth().getUid()).putFile(imageUri);
            uploadTask.addOnFailureListener(exception -> {
                listener.onCompletion();
            }).addOnSuccessListener(taskSnapshot -> {
                listener.onCompletion();
            });
        } else {
            listener.onCompletion();
        }
    }

    public static void uploadProductImage(Uri imageUri, String productKey, OnCompletionListener listener) {
        if (imageUri != null) {

            UploadTask uploadTask = getProductsImageRef(productKey).putFile(imageUri);
            uploadTask.addOnFailureListener(exception -> {
                listener.onCompletion();
            }).addOnSuccessListener(taskSnapshot -> {
                listener.onCompletion();
            });
        } else {
            listener.onCompletion();
        }
    }

    public static void login(String email, String password, OnLoginListener onLoginListener) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "loginWithEmail:success");
                        FirebaseUser user = getAuth().getCurrentUser();
                        onLoginListener.onComplete(true);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "loginWithEmail:failure", task.getException());
                        onLoginListener.onComplete(false);
                    }
                });
    }


    public static String getFamilyOrderId(String productId, String clientId) {

        int result = productId.compareTo(clientId);
        if (result < 0) {
            return productId + clientId;
        } else {
            return clientId + productId;
        }
    }

    public static String getClientOrderId(String productId, String familyId) {

        int result = productId.compareTo(familyId);
        if (result < 0) {
            return productId + familyId;
        } else {
            return familyId + productId;
        }
    }


}
