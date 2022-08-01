package xyz.ummo.bite

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class UmmoBiteApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /** Facebook App Events:
         *  This block below is for logging app events using the Facebook Android SDK **/
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

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