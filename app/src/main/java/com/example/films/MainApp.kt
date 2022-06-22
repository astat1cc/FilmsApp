package com.example.films

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.films.di.AppComponent
import com.example.films.di.DaggerAppComponent
import com.example.films.di.RoomModule

class MainApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().roomModule(RoomModule(this.applicationContext)).build()
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
    get() = requireContext().appComponent