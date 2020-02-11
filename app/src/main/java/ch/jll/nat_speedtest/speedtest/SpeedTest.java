package ch.jll.nat_speedtest.speedtest;

import android.os.AsyncTask;
import java.math.BigDecimal;
import java.math.RoundingMode;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

/**
 * Inheriting from the Async Class.
 * In order to perform requests, we need to use the AsyncTask Class bzw. the doInBackground Method
 */
public class SpeedTest extends AsyncTask<String, Void, Object> {
    /**
     * This Class uses a Speedtest library.
     * It comes with the SpeedTestSocket Class and
     * three Listeners: onCompletion, onError and onProgress.     *
     * We also the Methods startDownload and startUpload of the SpeedTestSccket class.
     */

    private SpeedTestSocket speedTestSocket = new SpeedTestSocket();

    /**
     * Contains an Object(implements SpeedTestCallback) to pass the SpeedTest's Data
     */
    private SpeedTestCallback callback;

    /**
     * @param callback the Object to pass the SpeedTest's Data
     */
    public SpeedTest(SpeedTestCallback callback) {
        this.callback = callback;
    }

    /**
     * the doInBackground Method gets inheritet from the AsyncTask class
     * it performs it's actions on a second thread
     *
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {

        /**
         * adding Listeners
         */
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                BigDecimal result = report.getTransferRateBit().divide(new BigDecimal(1000000)).setScale(3, RoundingMode.DOWN);
                callback.speedTestResult(result.toString());
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                System.out.println(errorMessage);
                callback.speedTestResult("Error");
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
            }
        });

        /**
         *
         * Performing the Requests, which responses get caught by the Listeners above.
         */
        if (strings[0] == "Down") {
            speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/10M.iso");
        } else {
            speedTestSocket.startUpload("http://ipv4.ikoula.testdebit.info/", 1000000);
        }
        return null;
    }
}
