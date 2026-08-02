@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package ir.tapsell.sample.ui.screens.preroll

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import ir.tapsell.mediation.taproll.exo.TaprollAdLoader
import ir.tapsell.sample.R
import ir.tapsell.sample.model.PreRollContainer
import ir.tapsell.sample.ui.components.LogText
import ir.tapsell.sample.ui.theme.TapsellSampleTheme
import ir.tapsell.shared.SampleVideosUrl
import ir.tapsell.shared.R as ShR

private const val BUTTON_WIDTH = 0.5F

@OptIn(ExperimentalMaterial3Api::class, UnstableApi::class)
@Composable
fun PreRollScreen(
    modifier: Modifier = Modifier,
    viewModel: PreRollViewModel = viewModel(),
) {
    val context = LocalActivity.current as Activity

    val exoplayer = remember(viewModel.adViewContainer, viewModel.renderer.value) {
        val adContainer = viewModel.adViewContainer.value
        when (viewModel.renderer.value) {
            Renderer.IMA -> {
                ExoPlayer.Builder(context)
                    .setMediaSourceFactory(DefaultMediaSourceFactory(context))
                    .build()
                    .apply {
                        adContainer?.playerView?.player = this
                        playWhenReady = true
                        prepare()
                        addListener(playerDefaultListener)
                    }
            }

            Renderer.Taproll -> {
                val taprollAdLoader = TaprollAdLoader.Builder(context)
                    .setAdEventListener { event ->
                        android.util.Log.d("PreRollScreen", "taproll event: ${event.getType()}")
                    }
                    .setAdErrorListener { error ->
                        android.util.Log.e("PreRollScreen", "taproll error: ${error.getType()} - ${error.getMessage()}")
                    }
                    .build()

                val mediaSourceFactory = DefaultMediaSourceFactory(context)
                    .setLocalAdInsertionComponents(
                        { taprollAdLoader },
                        adContainer?.playerView ?: error("playerView not available")
                    )

                ExoPlayer.Builder(context)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build()
                    .apply {
                        adContainer?.playerView?.player = this
                        playWhenReady = true
                        prepare()
                        addListener(playerDefaultListener)
                    }
                    .also { taprollAdLoader.setPlayer(it) }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.destroyAds(exoplayer)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(ShR.string.preroll))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = viewModel.renderer.value == Renderer.IMA,
                    onClick = { viewModel.renderer.value = Renderer.IMA },
                    label = { Text("IMA") },
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = viewModel.renderer.value == Renderer.Taproll,
                    onClick = { viewModel.renderer.value = Renderer.Taproll },
                    label = { Text("Taproll") }
                )
            }

            TapsellPlusPreRollView(
                modifier = modifier.wrapContentSize(),
                onUpdate = viewModel::updateAdContainer
            )

            Button(
                modifier = Modifier.fillMaxWidth(BUTTON_WIDTH),
                onClick = {
                    when (viewModel.renderer.value) {
                        Renderer.IMA -> viewModel.requestAd(context, exoplayer)
                        Renderer.Taproll -> viewModel.requestAdForTaproll(context)
                    }
                }
            ) {
                Text(text = stringResource(ShR.string.request))
            }

            Button(
                modifier = Modifier.fillMaxWidth(BUTTON_WIDTH),
                enabled = viewModel.isShowButtonEnabled.value,
                onClick = {
                    when (viewModel.renderer.value) {
                        Renderer.IMA -> viewModel.showVideo()
                        Renderer.Taproll -> {
                            if (viewModel.vastTag.isEmpty()) {
                                android.util.Log.e("PreRollScreen", "no vastTag found")
                                return@Button
                            }
                            val adTagUri = viewModel.vastTag.toUri()
                            val mediaItem = MediaItem.Builder()
                                .setUri(SampleVideosUrl.random())
                                .setAdsConfiguration(MediaItem.AdsConfiguration.Builder(adTagUri).build())
                                .build()
                            exoplayer.setMediaItem(mediaItem)
                        }
                    }
                }
            ) {
                Text(text = stringResource(ShR.string.show))
            }

            Button(
                modifier = Modifier.fillMaxWidth(BUTTON_WIDTH),
                enabled = viewModel.isShowButtonEnabled.value,
                onClick = exoplayer::restartPlayer
            ) {
                Text(text = stringResource(ShR.string.replay_video))
            }

            LogText(viewModel.log.value)
        }
    }
}

@Composable
private fun TapsellPlusPreRollView(
    modifier: Modifier = Modifier,
    onUpdate: (PreRollContainer) -> Unit = {},
) {
    val context = LocalActivity.current as Activity
    AndroidView(
        modifier = modifier,
        factory = {
            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.preroll_container, null, false)
            val frameLayout = view.findViewById<ViewGroup>(R.id.ad_container)
            frameLayout.also {
                onUpdate(
                    PreRollContainer.from(
                        player = it.findViewById(R.id.video_player_container),
                        companion = it.findViewById(R.id.companion_ad_slot),
                        playerView = it.findViewById(R.id.exo_player)
                    )
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreRollScreenPreview() {
    TapsellSampleTheme {
        PreRollScreen()
    }
}
