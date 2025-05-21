package ir.tapsell.sample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.mediation.Tapsell;
import ir.tapsell.mediation.ad.AdStateListener;
import ir.tapsell.mediation.ad.request.RequestResultListener;
import ir.tapsell.mediation.ad.show.AdShowCompletionState;
import ir.tapsell.mediation.ad.views.ntv.NativeAdViewContainer;
import ir.tapsell.shared.ConsoleView;
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys;

public class NativeBannerActivity extends AppCompatActivity {

    private static final String TAG = "NativeBannerActivity";
    private Button showButton;
    private Button destroyButton;
    private ConsoleView logTextView;
    private NativeAdViewContainer container;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_banner);
        init();
    }

    private void init() {
        createAdContainer();
        logTextView = findViewById(R.id.log_text_view);
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        showButton.setEnabled(false);
        destroyButton = findViewById(R.id.destroy_button);
        destroyButton.setEnabled(false);
        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
        destroyButton.setOnClickListener(v -> destroyAd());
    }

    private void createAdContainer() {
        container = new NativeAdViewContainer(this);
        FrameLayout adContainer = findViewById(R.id.adContainer);
        adContainer.addView(container);
    }

    private void requestAd() {
        Tapsell.requestNativeAd(
                TapsellMediationKeys.INSTANCE.getNative(),
                new RequestResultListener() {
                    @Override
                    public void onSuccess(@NonNull String adId) {
                        if (isDestroyed()) return;
                        responseId = adId;
                        showButton.setEnabled(true);
                        log("response", Log.DEBUG);
                    }

                    @Override
                    public void onFailure(@NonNull String message) {
                        if (isDestroyed()) return;
                        log("onFailure: " + message, Log.ERROR);
                    }
                });
    }

    private void showAd() {
        Tapsell.showNativeAd(responseId, container, this,
                new AdStateListener.Native() {
                    @Override
                    public void onAdImpression() {
                        destroyButton.setEnabled(true);
                        log("onAdImpression", Log.DEBUG);
                    }

                    @Override
                    public void onAdClicked() {
                        log("onAdClicked", Log.DEBUG);
                    }

                    @Override
                    public void onAdFailed(@NonNull String message) {
                        log("onAdFailed" + message, Log.ERROR);
                    }

                    @Override
                    public void onAdClosed(@NonNull AdShowCompletionState adShowCompletionState) {
                        log("onAdClosed: " + adShowCompletionState.name(), Log.DEBUG);
                    }
                });
        showButton.setEnabled(false);
    }

    private void log(String message, int logLevel) {
        if (logLevel == Log.ERROR) Log.e(TAG, message);
        else Log.d(TAG, message);
        logTextView.append("\n".concat(message));
    }

    private void destroyAd() {
        destroyButton.setEnabled(false);
        Tapsell.destroyNativeAd(responseId);
    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
