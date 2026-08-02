package ir.tapsell.sample.home

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import ir.tapsell.mediation.Tapsell
import ir.tapsell.sample.R

class HomeViewModel : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun setUserConsent(activity: Activity, consent: Boolean) {
        Tapsell.setInitializationListener {
            Tapsell.setUserConsent(activity, consent)
            printAppIds(activity)
        }
    }

    private fun printAppIds(activity: Activity) = getAppIds(activity).let { appIds ->
        Log.d(TAG, "onInitializationComplete: ${appIds.toList()}")
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            "onInitializationComplete: ${appIds.joinToString()}",
            Snackbar.LENGTH_LONG
        ).apply {
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                .setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    activity.resources.getDimension(R.dimen.h7)
                )
            show()
        }
    }

    /**
     * @return Array of app ids added in manifest: [Tapsell], [Admob]
     */
    private fun getAppIds(context: Context): Array<String?> {
        context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        ).metaData?.let { manifest ->
            return arrayOf(
                manifest.getString("ir.tapsell.mediation.APPLICATION_KEY"),
                manifest.getString("com.google.android.gms.ads.APPLICATION_ID"),
            )
        }
        return emptyArray()
    }
}
