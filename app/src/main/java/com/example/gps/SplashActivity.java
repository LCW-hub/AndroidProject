package com.example.gps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // 2초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 상태바 투명하게 설정
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        window.setNavigationBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        
        setContentView(R.layout.activity_splash);

        // 애니메이션 적용
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvAppName = findViewById(R.id.tvAppName);
        TextView tvSubTitle = findViewById(R.id.tvSubTitle);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ivLogo.startAnimation(fadeIn);
        tvAppName.startAnimation(fadeIn);
        tvSubTitle.startAnimation(fadeIn);

        // 2초 후 메인 화면으로 전환
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MapsActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
} 