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

public class BandWithTest extends AsyncTask<String, Void, Object> {
    private String zipCode, city, street, houseNumber;
    private SpeedTestCallback callback;
    final private String swisscomUrl = "https://www.swisscom.ch/map-api/onlinenslg/lineinfo";


    public BandWithTest(String zipCode, String city, String street, String houseNumber, SpeedTestCallback callback) {
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(String... strings) {
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

        URL url = null;
        try {
            url = new URL(swisscomUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect(); // Note the connect() here
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(inputJson.toString());
            osw.flush();
            osw.close();

            int status = urlConnection.getResponseCode();
            Log.e("status", "" + status);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
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

    public void readStream(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader((in)));
        try {
            callback.speedTestResult(br.readLine());
            br.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
