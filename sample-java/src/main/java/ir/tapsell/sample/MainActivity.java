package ir.tapsell.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ir.tapsell.mediation.Tapsell;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTapsell();
        setClickListeners();
    }

    private void initTapsell() {
        Tapsell.setInitializationListener(() -> {
            Log.d(TAG, "onInitializationComplete");
            Tapsell.setUserConsent(this, true);
        });
    }

    private void setClickListeners() {
        findViewById(R.id.btRewardedVideo)
                .setOnClickListener(v -> startActivity(RewardedVideoActivity.class));
        findViewById(R.id.btInterstitial)
                .setOnClickListener(v -> startActivity(InterstitialActivity.class));
        findViewById(R.id.btNativeBanner)
                .setOnClickListener(v -> startActivity(NativeBannerActivity.class));
        findViewById(R.id.btStandardBanner)
                .setOnClickListener(v -> startActivity(StandardBannerActivity.class));
        findViewById(R.id.btPreroll)
                .setOnClickListener(v -> startActivity(PreRollActivity.class));
    }

    private void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}