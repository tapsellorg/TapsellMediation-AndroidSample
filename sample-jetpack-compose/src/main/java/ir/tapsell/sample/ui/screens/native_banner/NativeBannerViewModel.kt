package ir.tapsell.sample.ui.screens.native_banner

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.mediation.ad.views.ntv.NativeAdView
import ir.tapsell.mediation.ad.views.ntv.NativeAdViewContainer
import ir.tapsell.sample.R
import ir.tapsell.sample.base.BaseViewModel
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys

class NativeBannerViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "NativeBannerViewModel"
    }

    private var responseId: String? = null
    var isShowButtonEnabled by mutableStateOf(false)
        private set
    var canShowAd by mutableStateOf(false)
        private set
    var isDestroyButtonEnabled by mutableStateOf(false)
        private set

    fun requestAd() {
        // destroy previous ad if there is any to load new fresh ad
        responseId?.let { destroyAd() }
        Tapsell.requestNativeAd(
            TapsellMediationKeys.native,
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

    fun onShowClick() {
        if (responseId.isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        canShowAd = true
    }

    fun showAd(adContainer: NativeAdViewContainer?) {
        adContainer?.let { container ->
            Tapsell.showNativeAd(
                responseId,
                NativeAdView.Builder(container)
                    .withMedia(container.findViewById(R.id.tapsell_native_ad_media))
                    .withTitle(container.findViewById(R.id.tapsell_native_ad_title))
                    .withDescription(container.findViewById(R.id.tapsell_native_ad_description))
                    .withLogo(container.findViewById(R.id.tapsell_native_ad_logo))
                    .withCtaButton(container.findViewById(R.id.tapsell_native_ad_cta))
                    .withSponsored(container.findViewById(R.id.tapsell_native_ad_sponsored))
                    .build(),
                adContainer.context as Activity,
                object : AdStateListener.Native {
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

                    override fun onAdClosed(completionState: AdShowCompletionState) {
                        log(TAG, "onAdClosed: ${completionState.name}")
                    }
                })
            isShowButtonEnabled = false
        } ?: log(TAG, "adContainer is null", Log.ERROR)
    }

    fun destroyAd() {
        Tapsell.destroyNativeAd(responseId)
        responseId = null
        isDestroyButtonEnabled = false
        canShowAd = false
        log(TAG, "destroyAd")
    }
}