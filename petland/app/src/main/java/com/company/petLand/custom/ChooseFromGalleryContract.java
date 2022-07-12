package com.company.petLand.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChooseFromGalleryContract extends ActivityResultContract<Integer, Uri> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Integer input) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (intent != null && resultCode == Activity.RESULT_OK) {
            return intent.getData();
        } else {
            return null;
        }

    }
}
