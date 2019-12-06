package com.example.airplane;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    //AirPlane
    Button airPlaneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        airPlaneBtn = findViewById(R.id.AirPlane);

        airPlaneBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean state = isAirplaneMode();
                try {
                    WifiManager wifiManager;
                    wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                    wifiManager.setWifiEnabled(false);
                    ConnectivityManager dataManager;
                    dataManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    Method dataMtd = null;
                    try
                    {
                        dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                    } catch (NoSuchMethodException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dataMtd.setAccessible(true);
                    try
                    {
                        dataMtd.invoke(dataManager, false);
                    } catch (IllegalArgumentException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvocationTargetException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                    if (state)
                        toggleAirplaneMode(0, state);
                    else
                        toggleAirplaneMode(1, state);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                state = isAirplaneMode();
                if (state) {
                    airPlaneBtn.setText("On");
                } else {
                    airPlaneBtn.setText("Off");
                }
            }

            // Airplane mode version code
            @SuppressLint("NewApi")
            public void toggleAirplaneMode(int value, boolean state) {
                // toggle airplane mode
                // check the version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) { // if
                    // less
                    // then
                    // version
                    // 4.2

                    Settings.System.putInt(getContentResolver(),
                            Settings.System.AIRPLANE_MODE_ON, value);
                } else {
                    Settings.Global.putInt(getContentResolver(),
                            Settings.Global.AIRPLANE_MODE_ON, value);
                }
                // broadcast an intent to inform
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", !state);
                sendBroadcast(intent);
            }

            @SuppressLint("NewApi")
            public boolean isAirplaneMode() {
                // check the version
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {// if
                    // less
                    // than
                    // version
                    // 4.2

                    return Settings.System.getInt(getContentResolver(),
                            Settings.System.AIRPLANE_MODE_ON, 0) != 0;
                } else {
                    return Settings.Global.getInt(getContentResolver(),
                            Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

                }
            }
        });
    }
}