package com.johfpardo.marvelcharacters

import android.app.Application
import com.johfpardo.marvelcharacters.di.AppComponent
import com.johfpardo.marvelcharacters.di.AppComponentProvider
import com.johfpardo.marvelcharacters.di.DaggerAppComponent

class MCApplication: Application(), AppComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }

    override fun getAppComponent(): AppComponent = appComponent
}
