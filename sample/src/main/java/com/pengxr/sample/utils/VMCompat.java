package com.pengxr.sample.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

public class VMCompat {

    public static <T extends ViewModel> T get(@NonNull ViewModelStoreOwner owner,
                                              @NonNull Class<T> modelClass) {
        return new ViewModelProvider(owner).get(modelClass);
    }
}