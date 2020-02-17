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
            /**
             * Define Address and Port
             */
            String hostAddress = "www.swisscom.com";
            int port = 80;
            long timeToRespond = 0;

            /**
             *  Get InternetAddress by Hostname,
             *  Create new Socket on www.swisscom.com | 80
             */
            InetAddress inetAddress = InetAddress.getByName(hostAddress);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

            /**
             *  Open new Socketchannel (A selectable channel for stream-oriented connecting sockets.)
             */
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(true);

            Date start = new Date();

            /**
             * if connection of socketchannel on socketaddress works,
             * calculate ResponseTime (Ping)
             */
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
