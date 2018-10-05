package com.example.mahmoudsalaheldien.checkconnection;

import java.io.InputStream;
import java.net.URL;

public  class GetSpeedTestHostsHandler extends Thread {

    @Override
    public void run() {
        //Get latitude, longitude
        try {
            URL url = new URL("http://www.speedtest.net/speedtest-config.php");
            InputStream is = url.openStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}
