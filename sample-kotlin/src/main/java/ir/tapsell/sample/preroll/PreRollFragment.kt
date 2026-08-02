package ir.tapsell.sample.preroll

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import ir.tapsell.mediation.taproll.exo.TaprollAdLoader
import ir.tapsell.sample.R
import ir.tapsell.sample.databinding.FragmentPrerollBinding
import ir.tapsell.sample.utils.addChip
import ir.tapsell.shared.SampleVideosUrl
import ir.tapsell.shared.TapsellKeyProvider
import ir.tapsell.shared.ZoneType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class PreRollFragment : Fragment() {

    private var _binding: FragmentPrerollBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PreRollViewModel>()

    private lateinit var exoPlayer: ExoPlayer
    private var taprollAdLoader: TaprollAdLoader? = null
    private var renderer: Renderer = Renderer.IMA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPrerollBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val zones = TapsellKeyProvider.zonesFor(requireContext(), ZoneType.PRE_ROLL)
        zones.forEachIndexed { index, zone ->
            binding.zonesChips.addChip(requireContext(), zone.name, checked = index == 0) {
                binding.inputZone.setText(zone.id)
            }
        }
        binding.inputZone.setText(zones.firstOrNull()?.id)

        binding.rendererSelector.setOnCheckedChangeListener { _, checkedId ->
            renderer = when (checkedId) {
                R.id.ima -> Renderer.IMA
                R.id.taproll -> Renderer.Taproll
                else -> Renderer.IMA
            }
            initializePlayer()
        }

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
        when (renderer) {
            Renderer.IMA -> viewModel.requestAd(
                zoneId = binding.inputZone.text.toString(),
                container = binding.videoPlayerContainer,
                companionContainer = binding.companionContainer,
                videoPlayer = binding.exoPlayer,
                videoPath = SampleVideosUrl.random(),
            )

            Renderer.Taproll -> viewModel.requestAdForTaproll(
                zoneId = binding.inputZone.text.toString(),
            )
        }
    }

    private fun showAd() {
        when (renderer) {
            Renderer.IMA -> viewModel.showAd()
            Renderer.Taproll -> showTaprollAd()
        }
    }

    private fun showTaprollAd() {
        if (viewModel.vastTag.isEmpty()) {
            viewModel.log(TAG, "no vastTag found")
            return
        }

        val adTagUri = viewModel.vastTag.toUri()
        val mediaItem = MediaItem.Builder()
            .setUri(SampleVideosUrl.random())
            .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(adTagUri).build())
            .build()

        exoPlayer.setMediaItem(mediaItem)
    }

    private fun initializePlayer() {
        if (::exoPlayer.isInitialized) releasePlayer()

        when (renderer) {
            Renderer.IMA -> {
                exoPlayer = ExoPlayer.Builder(requireContext())
                    .setMediaSourceFactory(DefaultMediaSourceFactory(requireContext()))
                    .build()
                    .apply {
                        binding.exoPlayer.player = this
                        playWhenReady = true
                        prepare()
                        addListener(playerListener)
                    }
            }

            Renderer.Taproll -> {
                taprollAdLoader = TaprollAdLoader.Builder(requireContext())
                    .setAdEventListener { event ->
                        viewModel.log(TAG, "taproll event: ${event.getType()}")
                    }
                    .setAdErrorListener { error ->
                        viewModel.log(TAG, "taproll error: ${error.getType()} - ${error.getMessage()}")
                    }
                    .build()

                val mediaSourceFactory = DefaultMediaSourceFactory(requireContext())
                    .setLocalAdInsertionComponents(
                        { taprollAdLoader!! },
                        binding.exoPlayer
                    )

                exoPlayer = ExoPlayer.Builder(requireContext())
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build()
                    .apply {
                        binding.exoPlayer.player = this
                        playWhenReady = true
                        prepare()
                        addListener(playerListener)
                    }

                taprollAdLoader!!.setPlayer(exoPlayer)
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY -> viewModel.log(TAG, "onPlaybackStateChanged: STATE_READY")
                Player.STATE_ENDED -> {
                    viewModel.log(TAG, "onPlaybackStateChanged: STATE_ENDED")
                    playNextVideo(SampleVideosUrl.random())
                }

                else -> {}
            }
        }
    }

    private fun playNextVideo(url: String) {
        viewModel.log(TAG, "playNextVideo: $url")
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
        taprollAdLoader = null
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
    }
}
