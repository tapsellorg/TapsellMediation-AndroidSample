package ir.tapsell.sample.ui.screens.preroll

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.sample.base.BaseViewModel
import ir.tapsell.sample.model.PreRollContainer
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys

class PreRollViewModel : BaseViewModel() {

    var log = mutableStateOf("")

    private var preRollAds = mutableListOf<String>()
    var isShowButtonEnabled = mutableStateOf(false)
    var adViewContainer = mutableStateOf<PreRollContainer?>(null)

    fun requestAd(exoplayer: ExoPlayer) {
        addLog("requestAd")
        adViewContainer.value?.let { adContainer ->
            adContainer.playerView.player = exoplayer
            Tapsell.requestPreRollAd(
                TapsellMediationKeys.preRoll,
                adContainer.player,
                adContainer.companion,
                adContainer.playerView,
                SAMPLE_VIDEO_URL,
                object: RequestResultListener {

                    override fun onSuccess(adId: String) {
                        preRollAds.add(adId)
                        isShowButtonEnabled.value = true
                        log(TAG, "onSuccess: $adId")
                    }

                    override fun onFailure(message: String) {
                        addLog("onFailure: $message")
                        isShowButtonEnabled.value = false
                    }
                })
        } ?: addLog("AdViewContainer is null")
    }

    fun showVideo() {
        addLog("showVideo")
        if (preRollAds.isEmpty()) {
            addLog("preRollAds is empty")
            return
        }
        val responseId = preRollAds.shuffled().single().also { preRollAds.remove(it) }
        Tapsell.showPreRollAd(
            responseId,
            object : AdStateListener.PreRoll {
                override fun onVastAvailable(value: String) {
                    log(TAG, "onVastAvailable: $value")
                }

                override fun onAdImpression() {
                    log(TAG, "onAdImpression")
                }

                override fun onAdClicked() {
                    log(TAG, "onAdClicked")
                }

                override fun onAdClosed(completionState: AdShowCompletionState) {
                    log(TAG, "onAdClosed: ${completionState.name}")
                }

                override fun onAdFailed(message: String) {
                    log(TAG, "onAdFailed: $message", Log.ERROR)
                }
            })
        isShowButtonEnabled.value = false
    }

    private fun releasePlayer(player: ExoPlayer) {
        addLog("releasePlayer")
        adViewContainer.value?.playerView?.player = null
        player.release()
    }

    fun destroyAds(player: ExoPlayer) {
        if (preRollAds.isEmpty()) {
            log(TAG, "There is no adId to destroy", Log.ERROR)
            return
        }
        preRollAds.forEach { Tapsell.destroyPreRollAd(it) }
        preRollAds.clear()
        releasePlayer(player)
    }

    fun updateAdContainer(container: PreRollContainer) {
        adViewContainer.value = container
    }

    private fun addLog(message: String) {
        log.value = buildString {
            append(message)
            appendLine()
            append(log.value)
        }
    }

    companion object {
        private const val TAG = "PreRollViewModel"
        private const val SAMPLE_VIDEO_URL =
            "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4"
    }
}

val SAMPLE_VIDEO_URLS = listOf(
    "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4",
    "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
    "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
    "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
    "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
)

val ExoPlayer.playerDefaultListener
    get() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY ->
                    Log.d("ExoPlayer", "onPlaybackStateChanged: STATE_READY")

                Player.STATE_ENDED -> {
                    Log.d("ExoPlayer", "onPlaybackStateChanged: STATE_ENDED")
                    playNextVideo(SAMPLE_VIDEO_URLS.shuffled().random())
                }

                else -> {}
            }
        }
    }

fun ExoPlayer.playNextVideo(url: String) {
    Log.d("ExoPlayer", "playNextVideo: $url")
    setMediaItem(MediaItem.Builder().setUri(Uri.parse(url)).build())
    prepare()
}

fun ExoPlayer.restartPlayer() {
    seekTo(0)
    playWhenReady = true
}

