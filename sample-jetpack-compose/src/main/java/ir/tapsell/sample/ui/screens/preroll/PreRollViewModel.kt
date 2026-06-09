package ir.tapsell.sample.ui.screens.preroll

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.PreRollAdListener
import ir.tapsell.mediation.ad.request.RequestResultListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.sample.base.BaseViewModel
import ir.tapsell.sample.model.PreRollContainer
import ir.tapsell.shared.SampleVideosUrl
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys

enum class Renderer { IMA, Taproll }

class PreRollViewModel : BaseViewModel() {

    var log = mutableStateOf("")

    private var preRollAds = mutableListOf<String>()
    var isShowButtonEnabled = mutableStateOf(false)
    var adViewContainer = mutableStateOf<PreRollContainer?>(null)
    var renderer = mutableStateOf(Renderer.IMA)
    var vastTag: String = ""
        private set

    fun requestAd(exoplayer: ExoPlayer) {
        addLog("requestAd")
        adViewContainer.value?.let { adContainer ->
            adContainer.playerView.player = exoplayer
            Tapsell.requestPreRollAd(
                TapsellMediationKeys.preRoll,
                adContainer.player,
                adContainer.companion,
                adContainer.playerView,
                SampleVideosUrl.random(),
                object : RequestResultListener {

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

    fun requestAdForTaproll() {
        addLog("requestAdForTaproll")
        Tapsell.requestPreRollAd(
            TapsellMediationKeys.preRoll,
            object : RequestResultListener {
                override fun onSuccess(adId: String) {
                    preRollAds.add(adId)
                    isShowButtonEnabled.value = true
                    log(TAG, "onSuccess: $adId")
                    Tapsell.getPreRollVastUrl(
                        adId,
                        object : PreRollAdListener {
                            override fun onVastAvailable(value: String) {
                                vastTag = value
                                log(TAG, "vastTag received: $value")
                            }

                            override fun onAdFailed(message: String) {
                                log(TAG, "getPreRollVastUrl onFailure: $message", Log.ERROR)
                            }
                        })
                }

                override fun onFailure(message: String) {
                    addLog("onFailure: $message")
                    isShowButtonEnabled.value = false
                }
            })
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
    }
}


val ExoPlayer.playerDefaultListener
    get() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY ->
                    Log.d("ExoPlayer", "onPlaybackStateChanged: STATE_READY")

                Player.STATE_ENDED -> {
                    Log.d("ExoPlayer", "onPlaybackStateChanged: STATE_ENDED")
                    playNextVideo(SampleVideosUrl.random())
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
