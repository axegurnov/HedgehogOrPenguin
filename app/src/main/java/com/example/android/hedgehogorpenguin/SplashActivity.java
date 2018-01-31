package com.example.android.hedgehogorpenguin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Роман on 31.01.2018.
 */

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        String fontHP = "fonts/10927.ttf";
        String fontOR = "fonts/11185.ttf";

        TextView textHedgehog = (TextView) findViewById(R.id.hedgehog);
        TextView textOr = (TextView) findViewById(R.id.or);
        TextView textPenguin = (TextView) findViewById(R.id.penguin);

        Typeface typefaceHP = Typeface.createFromAsset(getAssets(), fontHP);
        Typeface typefaceOR = Typeface.createFromAsset(getAssets(), fontOR);

        textHedgehog.setTypeface(typefaceHP);
        textOr.setTypeface(typefaceOR);
        textPenguin.setTypeface(typefaceHP);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void start(View view) {
        Intent mainIntent = new Intent(SplashActivity.this, FullscreenActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }
}
