package ir.tapsell.sample.standard

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentActivity
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.BannerSize
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.views.banner.BannerContainer
import ir.tapsell.sample.BaseViewModel

class StandardBannerViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "StandardBannerViewModel"
    }

    var responseId: String? = null
        private set

    val tapsellBannerSizes = BannerSize.values()
    private var selectedBannerSize = BannerSize.BANNER_320_50

    val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedBannerSize = tapsellBannerSizes[position]
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun requestAd(zoneId: String) {
        Tapsell.requestBannerAd(zoneId, selectedBannerSize, object : RequestResultListener {
            override fun onFailure(message: String) {
                log(TAG, "onFailure: $message", Log.ERROR)
            }

            override fun onSuccess(adId: String) {
                responseId = adId
                log(TAG, "onSuccess: $adId")
            }
        })
    }

    fun showAd(activity: FragmentActivity, container: BannerContainer) {
        if (responseId.isNullOrEmpty()) {
            log(TAG, "adId is empty", Log.ERROR)
            return
        }
        Tapsell.showBannerAd(responseId, container, activity, object : AdStateListener.Banner {
            override fun onAdClicked() {
                log(TAG, "onAdClicked")
            }

            override fun onAdFailed(message: String) {
                log(TAG, "onAdFailed: $message", Log.ERROR)
            }

            override fun onAdImpression() {
                log(TAG, "onAdImpression")
            }
        })
    }
}
