package ir.tapsell.sample;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.UriKt;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerView;

import java.util.Collections;

import ir.tapsell.mediation.Tapsell;
import ir.tapsell.mediation.ad.AdStateListener;
import ir.tapsell.mediation.ad.PreRollAdListener;
import ir.tapsell.mediation.ad.request.RequestResultListener;
import ir.tapsell.mediation.ad.show.AdShowCompletionState;
import ir.tapsell.mediation.taproll.exo.TaprollAdLoader;
import ir.tapsell.shared.ConsoleView;
import ir.tapsell.shared.SampleVideosKt;
import ir.tapsell.shared.TapsellKeyProvider;
import ir.tapsell.shared.ZoneType;

@OptIn(markerClass = UnstableApi.class)
public class PreRollActivity extends AppCompatActivity {

    private static final String TAG = "PreRollActivity";

    private enum Renderer {IMA, TAPROLL}

    private Button showButton;
    private ConsoleView logTextView;
    private PlayerView playerView;
    private ViewGroup videoPlayerContainer;
    private FrameLayout companionContainer;

    private ExoPlayer exoPlayer;
    private TaprollAdLoader taprollAdLoader;
    private Renderer renderer = Renderer.IMA;
    private String vastTag = "";
    private String responseId;

    private final Player.Listener playerListener = new Player.Listener() {
        @Override
        public void onPlaybackStateChanged(int playbackState) {
            Player.Listener.super.onPlaybackStateChanged(playbackState);
            if (playbackState == Player.STATE_READY) {
                log("onPlaybackStateChanged: STATE_READY", Log.DEBUG);
            } else if (playbackState == Player.STATE_ENDED) {
                log("onPlaybackStateChanged: STATE_ENDED", Log.DEBUG);
                Collections.shuffle(SampleVideosKt.getSampleVideosUrl());
                playNextVideo(SampleVideosKt.getSampleVideosUrl().get(0));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preroll);
        init();
    }

    private void init() {
        logTextView = findViewById(R.id.log_text_view);
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        Button replayButton = findViewById(R.id.replay_button);
        playerView = findViewById(R.id.exo_player);
        videoPlayerContainer = findViewById(R.id.video_player_container);
        companionContainer = findViewById(R.id.companion_container);
        RadioGroup rendererSelector = findViewById(R.id.renderer_selector);

        showButton.setEnabled(false);

        rendererSelector.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.ima) {
                renderer = Renderer.IMA;
            } else if (checkedId == R.id.taproll) {
                renderer = Renderer.TAPROLL;
            }
            initializePlayer();
        });

        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
        replayButton.setOnClickListener(v -> restartPlayer());
    }

    private void requestAd() {
        if (renderer == Renderer.IMA) {
            requestAdForIma();
        } else {
            requestAdForTaproll();
        }
    }

    private void requestAdForIma() {
        Tapsell.requestPreRollAd(
                TapsellKeyProvider.zonesFor(ZoneType.PRE_ROLL).get(0).getId(),
                videoPlayerContainer,
                companionContainer,
                playerView,
                SampleVideosKt.getSampleVideosUrl().get(0),
                new RequestResultListener() {
                    @Override
                    public void onSuccess(@NonNull String adId) {
                        if (isDestroyed()) return;
                        responseId = adId;
                        log("onSuccess: " + adId, Log.DEBUG);
                        showButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        if (isDestroyed()) return;
                        log("onFailure: " + message, Log.ERROR);
                    }
                });
    }

    private void requestAdForTaproll() {
        Tapsell.requestPreRollAd(
                TapsellKeyProvider.zonesFor(ZoneType.PRE_ROLL).get(0).getId(),
                new RequestResultListener() {
                    @Override
                    public void onSuccess(@NonNull String adId) {
                        if (isDestroyed()) return;
                        responseId = adId;
                        log("onSuccess: " + adId, Log.DEBUG);
                        Tapsell.getPreRollVastUrl(adId, new PreRollAdListener() {
                            @Override
                            public void onVastAvailable(@NonNull String value) {
                                if (isDestroyed()) return;
                                vastTag = value;
                                log("vastTag received: " + value, Log.DEBUG);
                                showButton.setEnabled(true);
                            }

                            @Override
                            public void onAdFailed(@NonNull String message) {
                                if (isDestroyed()) return;
                                log("getPreRollVastUrl onFailure: " + message, Log.ERROR);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        if (isDestroyed()) return;
                        log("onFailure: " + message, Log.ERROR);
                    }
                });
    }

    private void showAd() {
        if (renderer == Renderer.IMA) {
            showImaAd();
        } else {
            showTaprollAd();
        }
    }

    private void showImaAd() {
        Tapsell.showPreRollAd(responseId, new AdStateListener.PreRoll() {
            @Override
            public void onVastAvailable(@NonNull String value) {
                log("onVastAvailable: " + value, Log.DEBUG);
            }

            @Override
            public void onAdImpression() {
                log("onAdImpression", Log.DEBUG);
            }

            @Override
            public void onAdClicked() {
                log("onAdClicked", Log.DEBUG);
            }

            @Override
            public void onAdFailed(@NonNull String message) {
                log("onAdFailed: " + message, Log.ERROR);
            }

            @Override
            public void onAdClosed(@NonNull AdShowCompletionState completionState) {
                log("onAdClosed: " + completionState.name(), Log.DEBUG);
            }
        });
        showButton.setEnabled(false);
    }

    private void showTaprollAd() {
        if (vastTag.isEmpty()) {
            log("no vastTag found", Log.ERROR);
            return;
        }

        Uri adTagUri = UriKt.toUri(vastTag);
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(SampleVideosKt.getSampleVideosUrl().get(0))
                .setAdsConfiguration(
                        new MediaItem.AdsConfiguration.Builder(adTagUri).build()
                )
                .build();

        exoPlayer.setMediaItem(mediaItem);
        showButton.setEnabled(false);
    }

    private void initializePlayer() {
        releasePlayer();

        if (renderer == Renderer.IMA) {
            initImaPlayer();
        } else {
            initTaprollPlayer();
        }

        playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare();
        exoPlayer.addListener(playerListener);
    }

    private void initImaPlayer() {
        exoPlayer = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(this))
                .build();
    }

    private void initTaprollPlayer() {
        taprollAdLoader = new TaprollAdLoader.Builder(this)
                .setAdEventListener(event ->
                        log("taproll event: " + event.getType(), Log.DEBUG))
                .setAdErrorListener(error ->
                        log("taproll error: " + error.getType() + " - " + error.getMessage(), Log.ERROR))
                .build();

        DefaultMediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(this);
        mediaSourceFactory.setLocalAdInsertionComponents(
                adsConfiguration -> taprollAdLoader,
                playerView
        );

        exoPlayer = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build();

        taprollAdLoader.setPlayer(exoPlayer);
    }

    private void playNextVideo(String url) {
        log("playNextVideo: " + url, Log.DEBUG);
        exoPlayer.setMediaItem(
                new MediaItem.Builder().setUri(Uri.parse(url)).build()
        );
        exoPlayer.prepare();
    }

    private void restartPlayer() {
        if (exoPlayer != null) {
            exoPlayer.seekTo(0);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playerView.setPlayer(null);
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.release();
            exoPlayer = null;
        }
        taprollAdLoader = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (responseId != null) {
            Tapsell.destroyPreRollAd(responseId);
        }
        releasePlayer();
    }

    private void log(String message, int logLevel) {
        if (logLevel == Log.ERROR) Log.e(TAG, message);
        else Log.d(TAG, message);
        logTextView.append("\n".concat(message));
    }
}
