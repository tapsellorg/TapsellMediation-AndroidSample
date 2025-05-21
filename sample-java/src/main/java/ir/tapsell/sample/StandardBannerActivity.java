package ir.tapsell.sample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.mediation.Tapsell;
import ir.tapsell.mediation.ad.AdStateListener;
import ir.tapsell.mediation.ad.request.BannerSize;
import ir.tapsell.mediation.ad.request.RequestResultListener;
import ir.tapsell.mediation.ad.views.banner.BannerContainer;
import ir.tapsell.shared.ConsoleView;
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys;

public class StandardBannerActivity extends AppCompatActivity {

    private static final String TAG = "StandardActivity";
    private Button showButton;
    private Button destroyButton;
    private String responseId;
    private ConsoleView logTextView;
    private BannerContainer container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_banner);
        init();
    }

    private void init() {
        createAdContainer();
        logTextView = findViewById(R.id.log_text_view);
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        destroyButton = findViewById(R.id.destroy_button);
        destroyButton.setEnabled(false);
        showButton.setEnabled(false);
        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
        destroyButton.setOnClickListener(v -> destroyAd());
    }

    private void createAdContainer() {
        container = new BannerContainer(this);
        FrameLayout adContainer = findViewById(R.id.adContainer);
        adContainer.addView(container);
    }

    private void requestAd() {
        Tapsell.requestBannerAd(
                TapsellMediationKeys.INSTANCE.getBanner(),
                BannerSize.BANNER_300_250,
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
                        log("onFailure" + message, Log.ERROR);
                    }
                });
    }

    private void showAd() {
        Tapsell.showBannerAd(responseId, container, this,
                new AdStateListener.Banner() {
                    @Override
                    public void onAdImpression() {
                        log("onAdImpression", Log.DEBUG);
                        destroyButton.setEnabled(true);
                    }

                    @Override
                    public void onAdClicked() {
                        log("onAdClicked", Log.DEBUG);
                    }

                    @Override
                    public void onAdFailed(@NonNull String message) {
                        log("onAdFailed" + message, Log.ERROR);
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
        Tapsell.destroyBannerAd(responseId);

    }

    @Override
    protected void onDestroy() {
        destroyAd();
        super.onDestroy();
    }
}
