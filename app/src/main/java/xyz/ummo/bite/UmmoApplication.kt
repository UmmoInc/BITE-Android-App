package xyz.ummo.bite

import android.app.Application
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import xyz.ummo.bite.BuildConfig

class UmmoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        EventBus.builder()
            // have a look at the index class to see which methods are picked up
            // if not in the index @Subscribe methods will be looked up at runtime (expensive)
           // .addIndex(MyEventBusIndex())
            .installDefaultEventBus()

    }


    init {

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

        }

    }

 }