package com.company.petLand.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;




public class AuthHelper {
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean validatePassword(EditText passwordEt, EditText passConfirm, Activity activity) {
        String password = passwordEt.getText().toString().trim();
        String passwordConfirm = passConfirm.getText().toString().trim();
        int passLength = password.length();
        if (passLength < 6) {
            Toast.makeText(activity, "Password is too short", Toast.LENGTH_SHORT).show();

            return false;
        } else if (passLength > 20) {
            Toast.makeText(activity, "Password is too Long", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.matches(".*\\s+.*")) {
            if (password.equals(passwordConfirm)) {
                return true;
            } else {
                Toast.makeText(activity, "Passwords don't match", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(activity, "No empty spaces allowed", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public static boolean validateEmail(EditText email,Activity activity) {

        if (email.getText().toString().isEmpty()) {
            Toast.makeText(activity, "enter email address", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            if (email.getText().toString().trim().matches(EMAIL_PATTERN)) {

                return true;
            } else {
                Toast.makeText(activity, "Invalid email address", Toast.LENGTH_SHORT).show();

                return false;
            }
        }

    }
    public static Boolean validateGender(Integer gender, Activity activity) {
        if (gender == null) {
            Toast.makeText(activity, "You didnot set Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static Boolean validateUserName(EditText usernameEt, Activity activity) {
        if (usernameEt.getText().toString().trim().length() < 4) {
            Toast.makeText(activity, "too short UserName", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    public static  boolean validatePhone(EditText phoneET, Context context) {
        if (TextUtils.isEmpty(phoneET.getText())) {
            Toast.makeText(context,"Please Enter Phone Numebr",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phoneET.getText()).matches();
        }
    }

    public static  boolean validateImage(Uri imageUri, Context context) {
        if (imageUri!=null) {
            return true;
        } else {
            return  false;
        }
    }
//    public static  boolean validateLocation(LatLng location, Context context) {
//        if (location!=null) {
//            return true;
//        } else {
//            Toast.makeText(context,"Please Set Your Location",Toast.LENGTH_SHORT).show();
//            return  false;
//        }
//    }

}
