package ch.jll.nat_speedtest.ui.bandwith_test;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.test_and_helpers.BandWithTest;
import ch.jll.nat_speedtest.test_and_helpers.SpeedTestCallback;
import ch.jll.nat_speedtest.ui.errorDialog.ErrorDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import org.json.JSONException;
import org.json.JSONObject;


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

        //Überprüfen, dass auch alle eingaben nicht leer sind.
        if (!zipCode.equals("") && !city.equals("") && !street.equals("") && !houseNumber.equals("")) {
            BandWithTest bandWithTest = new BandWithTest(zipCode, city, street, houseNumber, this);
            startLoading();
            bandWithTest.execute();
        } else {
            errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.error_message_emptyInput));
        }
    }

    /*
    Abstrakte Methode des SpeedTestCallback interfaces.
    Hier wird die Response von unserem POST Request gehandelt.
    Wir kriegen die Daten aus dem Interface, "zerstückeln" die Daten dann in kleine Happen und schreiben sie schlussendlich wieder in die Form rein
     */
    @Override
    public void speedTestResult(final String result) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                endLoading();
                TextView uploadText = getView().findViewById(R.id.bwUploadSpeed);
                TextView downloadText = getView().findViewById(R.id.bwDownloadSpeed);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    if (!jsonObj.isNull("broadbandInfo")) {
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

    //Getter und Setter für die Form
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

    //Damit resettet man die zwei Outputs für die Up-/Downloadraten
    private void resetOutput() {
        TextView upText = getView().findViewById(R.id.bwUploadSpeed);
        TextView downText = getView().findViewById(R.id.bwDownloadSpeed);
        upText.setText("...");
        downText.setText("...");
    }

    private void endLoading() {
        btnStartTest.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        btnStartTest.setBackgroundResource(R.drawable.rounded_button);
    }

    private void startLoading() {
        btnStartTest.setEnabled(false);
        btnStartTest.setBackgroundResource(R.drawable.disabled_rounded_button);
        progressBar.setVisibility(View.VISIBLE);
    }
}