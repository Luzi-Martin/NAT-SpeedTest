package ch.jll.nat_speedtest.speedtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Inheriting from the Async Class.
 * In order to perform requests, we need to use the AsyncTask Class respectively the doInBackground Method
 */
public class BandWithTest extends AsyncTask<String, Void, Object> {
    private String zipCode, city, street, houseNumber;
    /**
     * Contains an Object which implements SpeedTestCallback to pass the BandWithTest's Data
     */
    private SpeedTestCallback callback;
    final private String swisscomUrl = "https://www.swisscom.ch/map-api/onlinenslg/lineinfo";

    /**
     * @param callback the Object to pass the BandWithTest's Data
     */
    public BandWithTest(String zipCode, String city, String street, String houseNumber, SpeedTestCallback callback) {
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.callback = callback;
    }

    /**
     * the doInBackground Method gets inheritet from the AsyncTask class
     * it performs it's actions on a second thread
     *
     * @param strings
     * @return it returns null, because transmitt our data otherwhise
     */
    @Override
    protected Object doInBackground(String... strings) {
        /**
         * create the Json's for the Post-Request-Body
         */
        JSONObject address = new JSONObject();
        JSONObject inputJson = new JSONObject();

        try {
            address.put("zipCode4", this.zipCode);
            address.put("city", this.city);
            address.put("street", this.street);
            address.put("houseNumber", this.houseNumber);
            inputJson.put("language", "de");
            inputJson.put("address", address);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /**
         *
         * Form new URL
         */
        URL url = null;
        try {
            url = new URL(swisscomUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /**
         * create new HTTPURLConnection
         */
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            /**
             * configure the Request
             */
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            /**
             * connect to the outside endpoint respectively Swisscom-API
             */
            urlConnection.connect(); // Note the connect() here
            /**
             * open OutPutStream to the Connenction
             * create new OutputStreamWriter in order to access/edit the Request-Body
             */
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            /**
             * Write the Request Body
             */
            osw.write(inputJson.toString());
            osw.flush();
            osw.close();

            /**
             * open InputStream to the Connection for the Response
             */
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            /**
             * read InputStream
             */
            readStream(in);

            Log.e("msg", urlConnection.getResponseMessage());
            Log.e("code", "" + urlConnection.getResponseCode());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    /**
     *
     * @param in InputStream that will be read
     */
    public void readStream(InputStream in) {
        /**
         * New BufferedReader in Order to get the Data out of the InputStream
         */
        BufferedReader br = new BufferedReader(new InputStreamReader((in)));
        try {
            callback.speedTestResult(br.readLine());
            br.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
