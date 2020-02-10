package ch.jll.nat_speedtest.speedtest;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class SpeedTest extends AsyncTask<String, Void, Object> {
    SpeedTestSocket speedTestSocket = new SpeedTestSocket();

    SpeedTestCallback callback;

    public SpeedTest(SpeedTestCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {

        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTotalPacketSize());
                BigDecimal result = report.getTransferRateBit().divide(new BigDecimal(1000000)).setScale(3, RoundingMode.DOWN);
                callback.speedTestResult(result.toString());
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                // called when a download/upload error occur

                callback.speedTestResult("Error");
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                System.out.println("[PROGRESS] progress : " + percent + "%");
                System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
            }
        });
        if(strings[0] == "Down") {
            speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/10M.iso");
        } else {
            speedTestSocket.startUpload("http://ipv4.ikoula.testdebit.info/", 1000000);

        }
        return null;
    }
}
