package com.pengxr.sample.base

import android.app.Application

/**
 * Created by pengxr on 5/9/2021
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init statistics lib.
        init(applicationContext)
    }
}