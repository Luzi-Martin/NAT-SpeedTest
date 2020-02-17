package ch.jll.nat_speedtest.ui.bandwith_test;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.speedtest.BandWithTest;
import ch.jll.nat_speedtest.speedtest.SpeedTestCallback;
import ch.jll.nat_speedtest.ui.errorDialog.ErrorDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.speedtest.BandWithTest;
import ch.jll.nat_speedtest.speedtest.SpeedTestCallback;

import java.io.IOException;

/**
 *  Implements SpeedTestCallback to get the Callback of the BandWithTest
 */
public class BandwithTestFragment extends Fragment implements View.OnClickListener, SpeedTestCallback {

    private BandwithTestViewModel bandwithTestViewModel;
    private Button btnStartTest;
    private ErrorDialog errorDialog = new ErrorDialog();
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bandwithTestViewModel = ViewModelProviders.of(this).get(BandwithTestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bandwithtest, container, false);
        btnStartTest = root.findViewById(R.id.btnStart);
        btnStartTest.setOnClickListener(this);
        progressBar = root.findViewById(R.id.bandWithProgressBar);
        return root;
    }

    @Override
    public void onClick(View v) {
        if (!isNetworkConnectionAvailable()) {
            errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.error_message));
            return;
        }
        resetOutput();
        String zipCode, city, street, houseNumber;
        zipCode = getZipCode();
        city = getCity();
        street = getStreet();
        houseNumber = getHouseNumber();

        /**
         * Validate Input on Emptiness
         */
        if (!zipCode.equals("") && !city.equals("") && !street.equals("") && !houseNumber.equals("")) {
            BandWithTest bandWithTest = new BandWithTest(zipCode, city, street, houseNumber, this);
            startLoading();
            bandWithTest.execute();
        } else {
            errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.error_message_emptyInput));
        }
    }

    /**
     * Overridden from SpeedTestCallback
     */
    @Override
    public void speedTestResult(final String result) {
        /**
         * In order to Run on main Thread again
         */
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                endLoading();
                TextView uploadText = getView().findViewById(R.id.bwUploadSpeed);
                TextView downloadText = getView().findViewById(R.id.bwDownloadSpeed);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    if (!jsonObj.isNull("broadbandInfo")) {
                        /**
                         * Get Data from JSON, format Data
                         */
                        JSONObject broadBandInfo = new JSONObject(jsonObj.getJSONObject("broadbandInfo").toString());
                        uploadText.setText(String.format("%s", broadBandInfo.getString("maxUpSpeed")));
                        downloadText.setText(String.format("%s", broadBandInfo.getString("maxDownSpeed")));
                    } else {
                        errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.error_message_noAdress));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isNetworkConnectionAvailable() {
        ConnectivityManager connectivityService = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
    }

    /**
     * Getter for all EditText Elements in View
     *
     */

    private String getZipCode() {
        EditText plzEdit = getView().findViewById(R.id.inputPlz);
        return plzEdit.getText().toString();
    }

    private String getCity() {
        EditText cityEdit = getView().findViewById(R.id.inputPlace);
        return cityEdit.getText().toString();
    }

    private String getStreet() {
        EditText streetEdit = getView().findViewById(R.id.inputStreet);
        return streetEdit.getText().toString();
    }

    private String getHouseNumber() {
        EditText numberEdit = getView().findViewById(R.id.inputHouseNumber);
        return numberEdit.getText().toString();
    }

    /**
     * In order to reset the two Textview Elements for the Down- and Upload-Rates
     */
    private void resetOutput() {
        TextView upText = getView().findViewById(R.id.bwUploadSpeed);
        TextView downText = getView().findViewById(R.id.bwDownloadSpeed);
        upText.setText("...");
        downText.setText("...");
    }

    /**
     * Stop Loading-Spinner
     * Enable Button
     */
    private void endLoading() {
        btnStartTest.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        btnStartTest.setBackgroundResource(R.drawable.rounded_button);
    }

    /**
     * start Loading-Spinner
     * Disable Button
     */
    private void startLoading() {
        btnStartTest.setEnabled(false);
        btnStartTest.setBackgroundResource(R.drawable.disabled_rounded_button);
        progressBar.setVisibility(View.VISIBLE);
    }
}