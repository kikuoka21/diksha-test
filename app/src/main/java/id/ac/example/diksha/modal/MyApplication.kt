package id.ac.example.diksha.modal

import android.app.Application
import id.ac.example.diksha.AppComponent
import id.ac.example.diksha.DaggerAppComponent

import timber.log.Timber

class MyApplication : Application(){
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

//        appComponent.inject(this)

        Timber.plant(Timber.DebugTree())

        Timber.d("================================INITIAL timber===================================")
    }
}