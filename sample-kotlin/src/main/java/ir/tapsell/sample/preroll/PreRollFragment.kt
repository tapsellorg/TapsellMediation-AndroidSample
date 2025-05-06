package ir.tapsell.sample.preroll

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import ir.tapsell.sample.databinding.FragmentPrerollBinding
import ir.tapsell.sample.utils.addChip
import ir.tapsell.shared.TapsellKeys.LegacyKeys
import ir.tapsell.shared.TapsellPreRollAdNetworks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PreRollFragment : Fragment() {

    private var _binding: FragmentPrerollBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PreRollViewModel>()

    private lateinit var exoPlayer: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrerollBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TapsellPreRollAdNetworks.map { adNetwork ->
            binding.chipAdNetworks.addChip(requireContext(), adNetwork.name) {
                binding.inputZone.setText(adNetwork.preRoll)
            }
        }
        binding.inputZone.setText(LegacyKeys.preRoll)

        binding.btnRequest.setOnClickListener {
            requestAd()
        }
        binding.btnShow.setOnClickListener {
            showAd()
        }
        binding.btnReplay.setOnClickListener {
            restartPlayer()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.logMessage.collect {
                binding.tvLog.text = it
            }
        }
    }

    private fun requestAd() {
        viewModel.requestAd(
            zoneId = binding.inputZone.text.toString(),
            container = binding.videoPlayerContainer,
            companionContainer = binding.companionContainer, // optional
            videoPlayer = binding.exoPlayer,
            videoPath = SAMPLE_VIDEO_URL,
        )
    }

    private fun showAd() {
        viewModel.showAd()
    }

    private fun initializePlayer() {
        if (::exoPlayer.isInitialized) releasePlayer()

        exoPlayer = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(DefaultMediaSourceFactory(requireContext())) // optional
            .build()
            .apply {
                binding.exoPlayer.player = this
                playWhenReady = true
                prepare()
                addListener(playerListener)
            }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY -> Log.d(TAG, "onPlaybackStateChanged: STATE_READY")
                Player.STATE_ENDED -> {
                    Log.d(TAG, "onPlaybackStateChanged: STATE_ENDED")
                    playNextVideo(SAMPLE_VIDEO_URLS.shuffled().random())
                }

                else -> {}
            }
        }
    }

    private fun playNextVideo(url: String) {
        Log.d(TAG, "playNextVideo: $url")
        exoPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri(Uri.parse(url))
                .build()
        )
        exoPlayer.prepare()
    }

    private fun releasePlayer() {
        binding.exoPlayer.player = null
        exoPlayer.apply {
            playWhenReady = false
            release()
        }
    }

    private fun restartPlayer() = exoPlayer.apply {
        seekTo(0)
        playWhenReady = true
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyAds()
        releasePlayer()
    }

    companion object {
        private const val TAG = "PreRollFragment"
        private const val SAMPLE_VIDEO_URL =
            "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4"

        private val SAMPLE_VIDEO_URLS = listOf(
            SAMPLE_VIDEO_URL,
            "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
        )
    }
}
