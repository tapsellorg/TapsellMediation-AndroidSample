package ir.tapsell.sample

import android.app.Application
import timber.log.Timber

class TapsellSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, "Timber $tag", message, t)
            }
        })
    }
}
