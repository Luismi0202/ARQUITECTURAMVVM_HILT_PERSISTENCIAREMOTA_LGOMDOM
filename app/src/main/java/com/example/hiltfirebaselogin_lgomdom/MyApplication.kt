// File: `app/src/main/java/com/example/hiltfirebaselogin_lgomdom/MyApplication.kt`
package com.example.hiltfirebaselogin_lgomdom

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
