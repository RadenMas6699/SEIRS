/*
 * Created by RadenMas on 29/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.seirs

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by RadenMas on 29/08/2022.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(false)
    }
}