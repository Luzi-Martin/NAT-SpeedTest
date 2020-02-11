package ch.jll.nat_speedtest.ui.mobile_test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.speedtest.Ping;
import ch.jll.nat_speedtest.speedtest.SpeedTest;
import ch.jll.nat_speedtest.speedtest.SpeedTestCallback;
import ch.jll.nat_speedtest.ui.errorDialog.ErrorDialog;

/**
 * Implements SpeedTestCallback to get the Callback of the SpeedTest
 */
public class MobileTestFragment extends Fragment implements SpeedTestCallback, View.OnClickListener {

    private MobileViewModel mobileViewModel;
    private View root;
    private Button btnStartTest;
    private String download = "";
    private String upload = "";
    private TextView txtDownload;
    private TextView txtUpload;
    private TextView txtPing;
    private ErrorDialog errorDialog = new ErrorDialog();
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mobileViewModel = ViewModelProviders.of(this).get(MobileViewModel.class);
        root = inflater.inflate(R.layout.fragment_mobiletest, container, false);
        btnStartTest = root.findViewById(R.id.btnStartMobileTest);
        btnStartTest.setOnClickListener(this);
        txtDownload = root.findViewById(R.id.maxMobileDownload);
        txtUpload = root.findViewById(R.id.maxMobileUpload);
        txtPing = root.findViewById(R.id.ping);
        progressBar = root.findViewById(R.id.speedTestProgressBar);
        return root;
    }

    /**
     * Inherited from SpeedTestCallback
     * gets called from SpeedTest to transmit its Data
     *
     * @param result data from SpeedTest
     */
    @Override
    public void speedTestResult(final String result) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(result.contains("ping:")) {
                    txtPing.setText(result.replace("ping:", ""));
                    return;
                }
                if (result == "Error") {
                    errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.failed));
                    endLoading();
                    return;
                }
                if (download == "") {
                    download = result + " Mbit/s";
                    getUploadSpeed();
                } else if (upload == "") {
                    upload = result + " Mbit/s";
                    txtDownload.setText(download);
                    txtUpload.setText(upload);
                    download = "";
                    upload = "";
                    endLoading();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!isNetworkConnectionAvailable()) {
            errorDialog.showAlertDialog(getContext(), getResources().getString(R.string.error_message));
            return;
        }
        txtDownload.setText("...");
        txtUpload.setText("...");
        txtPing.setText("...");
        startLoading();
        getDownloadSpeed();
        getPing();
    }

    private void getDownloadSpeed() {
        SpeedTest speedtest = new SpeedTest(this);
        speedtest.execute("Down");
    }

    private void getUploadSpeed() {
        SpeedTest speedtest = new SpeedTest(this);
        speedtest.execute("Up");
    }

    private void getPing() {
        Ping ping = new Ping(this);
        ping.execute();
    }

    private boolean isNetworkConnectionAvailable() {
        ConnectivityManager connectivityService = (ConnectivityManager)
                getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
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

