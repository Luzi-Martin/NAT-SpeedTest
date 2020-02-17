package ch.jll.nat_speedtest.test_and_helpers;

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
import java.nio.charset.StandardCharsets;


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
        //Zwei leere JSON-Objekte, die wir für den POST Request benutzen
        JSONObject address = new JSONObject();
        JSONObject inputJson = new JSONObject();

        try {
            //Daten in die JSON-Objekte einfügen
            address.put("zipCode4", this.zipCode);
            address.put("city", this.city);
            address.put("street", this.street);
            address.put("houseNumber", this.houseNumber);
            inputJson.put("language", "de");
            inputJson.put("address", address);
        } catch (Exception e) {
            //Errors catchen
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
            //Die Verbindung zur SwissomUrl öffnen
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //Voreinstellungen einstellen
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect(); // Note the connect() here
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);

            osw.write(inputJson.toString());
            osw.flush();
            osw.close();

            //Inputstream initialisieren für die Antwort vom Swisscom-Server
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
            //Repsonse Message (OK, Forbidden, No Connection usw) und Reponse Code (200, 404, 500) werden hier auf der Konsole ausgegeben
            Log.e("msg", urlConnection.getResponseMessage());
            Log.e("code", "" + urlConnection.getResponseCode());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    //Response vom Swisscom-Server auslesen und abspeichern
    private void readStream(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader((in)));
        try {
            callback.speedTestResult(br.readLine());
            br.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
