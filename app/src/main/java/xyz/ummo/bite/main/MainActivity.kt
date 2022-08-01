package xyz.ummo.bite.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import timber.log.Timber
import xyz.ummo.bite.BuildConfig
import xyz.ummo.bite.R

/*import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;*/

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFM = supportFragmentManager
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            /** By default, the [FacebookSdk] doesn't log tokens to logcat - to avoid leaking user
             * tokens **/
            FacebookSdk.setIsDebugEnabled(true)
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_RAW_RESPONSES)
            FacebookSdk.addLoggingBehavior(LoggingBehavior.GRAPH_API_DEBUG_INFO)
        }

    }
    companion object {

        // tags used to attach the fragments

        lateinit var supportFM: FragmentManager

    }

}