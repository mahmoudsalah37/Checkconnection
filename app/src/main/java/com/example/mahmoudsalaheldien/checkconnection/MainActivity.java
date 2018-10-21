package com.example.mahmoudsalaheldien.checkconnection;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {
    private ImageView imageView;
    private TextView textView;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private View mFloatingView;
    private ImageView im;

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            imageView.setImageResource(R.drawable.bright);
            textView.setText(R.string.connected);
            im.setImageResource(R.drawable.bright);
        } else {
            imageView.setImageResource(R.drawable.extinguished);
            textView.setText(R.string.disconnected);
            im.setImageResource(R.drawable.extinguished);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InternetAvailabilityChecker.init(this);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        im = mFloatingView.findViewById(R.id.collapsed_iv);
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        new Thread() {
                            @Override
                            public void run() {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        InternetAvailabilityChecker.init(MainActivity.this);
                                        InternetAvailabilityChecker mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
                                        mInternetAvailabilityChecker.addInternetConnectivityListener(MainActivity.this);
                                        mInternetAvailabilityChecker.onNetworkChange(true);
                                    }
                                });
                                super.run();
                            }
                        }.start();
                    }
                }, 0, 5000);
        initializeView();

    }

    private void initializeView() {
        findViewById(R.id.Background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {
                    FloatingViewService.mFloatingView = mFloatingView;
                    startService(new Intent(MainActivity.this, FloatingViewService.class));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
