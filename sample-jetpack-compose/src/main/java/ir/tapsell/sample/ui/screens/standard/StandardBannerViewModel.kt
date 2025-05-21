package ir.tapsell.sample.ui.screens.standard

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.BannerSize
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.views.banner.BannerContainer
import ir.tapsell.sample.base.BaseViewModel
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys

class StandardBannerViewModel : BaseViewModel() {

    private var responseId: String? = null
    var isShowButtonEnabled by mutableStateOf(false)
        private set
    var isDestroyButtonEnabled by mutableStateOf(false)
        private set
    private var adContainer by mutableStateOf<BannerContainer?>(null)
    private var bannerSize by mutableStateOf(BannerSize.BANNER_320_50)

    companion object {
        private const val TAG = "StandardBannerViewModel"
    }

    fun requestAd() {
        Tapsell.requestBannerAd(
            TapsellMediationKeys.banner, bannerSize,
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

    fun showAd(activity: Activity) {
        if (responseId.isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        adContainer?.let { container ->
            Tapsell.showBannerAd(responseId, container, activity, object : AdStateListener.Banner {
                override fun onAdClicked() {
                    log(TAG, "onAdClicked")
                }

                override fun onAdFailed(message: String) {
                    log(TAG, "onAdFailed: $message", Log.ERROR)
                }

                override fun onAdImpression() {
                    isDestroyButtonEnabled = true
                    log(TAG, "onAdImpression")
                }
            })
            isShowButtonEnabled = false
        } ?: log(TAG, "adContainer is null", Log.ERROR)
    }

    fun destroyAd() {
        Tapsell.destroyBannerAd(responseId)
        isDestroyButtonEnabled = false
        log(TAG, "destroyAd")
    }

    fun updateContainer(container: BannerContainer) {
        this.adContainer = container
    }
}
