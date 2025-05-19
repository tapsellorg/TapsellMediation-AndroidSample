package ir.tapsell.sample.preroll

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.sample.BaseViewModel

class PreRollViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "PreRollViewModel"
    }

    private var preRollAds = mutableListOf<String>()

    fun requestAd(
        zoneId: String,
        container: ViewGroup,
        companionContainer: ViewGroup?,
        videoPlayer: FrameLayout,
        videoPath: String?,
    ) {
        Tapsell.requestPreRollAd(
            zoneId,
            container,
            companionContainer,
            videoPlayer,
            videoPath,
            object : RequestResultListener {
                override fun onFailure(message: String) {
                    log(TAG, "onFailure: $message", Log.ERROR)
                }

                override fun onSuccess(adId: String) {
                    preRollAds.add(adId)
                    log(TAG, "onSuccess: $adId")
                }
            })
    }

    fun showAd() {
        if (preRollAds.lastOrNull().isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        Tapsell.showPreRollAd(
            preRollAds.last(),
            object : AdStateListener.PreRoll {
                override fun onAdClicked() {
                    log(TAG, "onAdClicked")
                }

                override fun onAdClosed(completionState: AdShowCompletionState) {
                    log(TAG, "onAdClosed: ${completionState.name}")
                }

                override fun onVastAvailable(vastUrl: String) {
                    log(TAG, "onVastAvailable: $vastUrl")
                }

                override fun onAdFailed(message: String) {
                    log(TAG, "onAdFailed: $message", Log.ERROR)
                }

                override fun onAdImpression() {
                    log(TAG, "onAdImpression")
                }
            })
    }

    fun destroyAds() {
        if (preRollAds.isEmpty()) {
            log(TAG, "There is no adId to destroy", Log.ERROR)
            return
        }
        preRollAds.forEach { Tapsell.destroyPreRollAd(it) }
        preRollAds.clear()
    }
}
