package ir.tapsell.sample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.mediation.Tapsell;
import ir.tapsell.mediation.ad.AdStateListener;
import ir.tapsell.mediation.ad.request.RequestResultListener;
import ir.tapsell.mediation.ad.show.AdShowCompletionState;
import ir.tapsell.shared.ConsoleView;
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys;

public class InterstitialActivity extends AppCompatActivity {
    private static final String TAG = "RewardActivity";
    private Button showButton;
    private ConsoleView logTextView;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        init();
    }

    private void init() {
        logTextView = findViewById(R.id.log_text_view);
        Button requestButton = findViewById(R.id.request_button);
        showButton = findViewById(R.id.show_button);
        showButton.setEnabled(false);
        requestButton.setOnClickListener(v -> requestAd());
        showButton.setOnClickListener(v -> showAd());
    }

    private void requestAd() {
        Tapsell.requestInterstitialAd(
                TapsellMediationKeys.INSTANCE.getInterstitial(),
                new RequestResultListener() {
                    @Override
                    public void onSuccess(@NonNull String adId) {
                        if (isDestroyed()) return;
                        responseId = adId;
                        log("response", Log.DEBUG);
                        showButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure() {
                        if (isDestroyed()) return;
                        log("onFailure", Log.ERROR);
                    }
                });
    }

    private void showAd() {
        Tapsell.showInterstitialAd(responseId, this,
                new AdStateListener.Interstitial() {
                    @Override
                    public void onAdClosed(@NonNull AdShowCompletionState adShowCompletionState) {
                        log("onAdClosed", Log.DEBUG);
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
}
