package com.company.petLand.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CameraContract extends ActivityResultContract<Intent, Boolean> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent intent) {

        return intent;
    }

    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return resultCode == Activity.RESULT_OK;

    }
}
