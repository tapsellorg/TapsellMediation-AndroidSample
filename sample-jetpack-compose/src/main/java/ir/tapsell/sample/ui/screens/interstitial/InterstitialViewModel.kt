package ir.tapsell.sample.ui.screens.interstitial

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.sample.base.BaseViewModel
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys


class InterstitialViewModel : BaseViewModel() {

    private var responseId: String? = null
    var isShowButtonEnabled by mutableStateOf(false)
        private set

    companion object {
        private const val TAG = "InterstitialViewModel"
    }

    fun requestAd() {
        Tapsell.requestInterstitialAd(
            TapsellMediationKeys.interstitial,
            object : RequestResultListener {
                override fun onFailure(message: String) {
                    log(TAG, "onFailure: $message", Log.ERROR)
                }

                override fun onSuccess(adId: String) {
                    responseId = adId
                    isShowButtonEnabled = true
                    log(TAG, "onSuccess: $adId")
                }
            })
    }

    fun showAd(activity: Activity?) {
        if (activity == null ) {
            log(TAG, "activity is null", Log.ERROR)
            return
        }
        if (responseId.isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        Tapsell.showInterstitialAd(responseId, activity, object : AdStateListener.Interstitial {
            override fun onAdClicked() {
                log(TAG, "onAdClicked")
            }

            override fun onAdClosed(completionState: AdShowCompletionState) {
                log(TAG, "onAdClosed: ${completionState.name}")
            }

            override fun onAdFailed(message: String) {
                log(TAG, "onAdFailed: $message", Log.ERROR)
            }

            override fun onAdImpression() {
                log(TAG, "onAdImpression")
            }
        })
        isShowButtonEnabled = false
    }
}
