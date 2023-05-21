package ir.tapsell.sample.interstitial

import android.util.Log
import androidx.fragment.app.FragmentActivity
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.sample.BaseViewModel

class InterstitialViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "InterstitialViewModel"
    }

    var responseId: String? = null
        private set


    fun requestAd(zoneId: String) {
        Tapsell.requestInterstitialAd(zoneId, object : RequestResultListener {
            override fun onFailure() {
                log(TAG, "onFailure", Log.ERROR)
            }

            override fun onSuccess(adId: String) {
                responseId = adId
                log(TAG, "onSuccess: $adId", Log.DEBUG)
            }

        })
    }

    fun showAd(activity: FragmentActivity) {
        if (responseId.isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        Tapsell.showInterstitialAd(responseId, activity, object : AdStateListener.Interstitial {
            override fun onAdClicked() {
                log(TAG, "onAdClicked", Log.DEBUG)
            }

            override fun onAdClosed(completionState: AdShowCompletionState) {
                log(TAG, "onAdClosed: ${completionState.ordinal}", Log.DEBUG)
            }

            override fun onAdFailed(message: String) {
                log(TAG, "onAdFailed: $message", Log.ERROR)
            }

            override fun onAdImpression() {
                log(TAG, "onAdImpression", Log.DEBUG)
            }
        })
    }
}
