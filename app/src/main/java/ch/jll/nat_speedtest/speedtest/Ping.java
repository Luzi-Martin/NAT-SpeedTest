package ch.jll.nat_speedtest.speedtest;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Ping extends AsyncTask<String, Void, Object> {
    private SpeedTestCallback callback;

    public Ping(SpeedTestCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            String hostAddress = "www.swisscom.com";
            int port = 80;
            long timeToRespond = 0;

            InetAddress inetAddress = InetAddress.getByName(hostAddress);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(true);

            Date start = new Date();
            if (sc.connect(socketAddress)) {
                Date stop = new Date();
                timeToRespond = (stop.getTime() - start.getTime());
            }

            callback.speedTestResult("ping:" + timeToRespond);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            callback.speedTestResult("ping:0");
        }
        return null;
    }
}
