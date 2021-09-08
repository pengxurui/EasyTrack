package com.pengxr.sample.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Copyright (C) 2005-2021 宁远科技股份有限公司
 * FileName:    VMCompat
 * Author:      liangy
 * Date:        2021/1/20
 * Description: ViewModel兼容类
 * history:
 */
public class VMCompat {

    public static <T extends ViewModel> T get(@NonNull ViewModelStoreOwner owner,
                                              @NonNull Class<T> modelClass) {
        if (false) {
            if (owner instanceof FragmentActivity) {
                FragmentActivity a = (FragmentActivity) owner;
                return ViewModelProviders.of(a).get(modelClass);
            } else if (owner instanceof Fragment) {
                Fragment f = (Fragment) owner;
                return ViewModelProviders.of(f).get(modelClass);
            }
            throw new IllegalArgumentException();
        } else {
            return new ViewModelProvider(owner).get(modelClass);
        }
    }
}