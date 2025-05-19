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

public class RewardedVideoActivity extends AppCompatActivity {

    private static final String TAG = "RewardedVideoActivity";
    private Button showButton;
    private ConsoleView logTextView;
    private String responseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_video);
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

        Tapsell.requestRewardedAd(
                TapsellMediationKeys.INSTANCE.getRewarded(),
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
        Tapsell.showRewardedAd(responseId, this,
                new AdStateListener.Rewarded() {
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
                    public void onRewarded() {
                        log("onRewarded", Log.DEBUG);
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
