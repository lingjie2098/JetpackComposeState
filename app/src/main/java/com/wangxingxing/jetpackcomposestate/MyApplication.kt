package com.wangxingxing.jetpackcomposestate

import android.app.Application
import timber.log.Timber

/**
 * Created by ChenJun on 2024/2/28.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}