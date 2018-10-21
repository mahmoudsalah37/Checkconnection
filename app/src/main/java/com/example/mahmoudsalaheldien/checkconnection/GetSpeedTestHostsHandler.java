package com.example.mahmoudsalaheldien.checkconnection;

import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public  class GetSpeedTestHostsHandler extends Thread {

    @Override
    public void run() {
        //Get latitude, longitude
        try {
            URL url = new URL("https://www.google.com/");
            url.openStream();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
