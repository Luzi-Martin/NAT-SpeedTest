package ch.jll.nat_speedtest.ui.mobile_test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.speedtest.SpeedTest;
import ch.jll.nat_speedtest.speedtest.SpeedTestCallback;

public class MobileTestFragment extends Fragment implements SpeedTestCallback, View.OnClickListener {

    private MobileViewModel mobileViewModel;
    private View root;
    private Button btnStartTest;
    private String download = "";
    private String upload = "";
    private TextView txtDownload;
    private TextView txtUpload;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mobileViewModel = ViewModelProviders.of(this).get(MobileViewModel.class);
        root = inflater.inflate(R.layout.fragment_mobiletest, container, false);
        final TextView textView = root.findViewById(R.id.text_mobileTest);
        mobileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        btnStartTest = root.findViewById(R.id.btnStartMobileTest);
        btnStartTest.setOnClickListener(this);

        txtDownload = root.findViewById(R.id.maxMobileDownload);
        txtUpload = root.findViewById(R.id.maxMobileUpload);

        return root;
    }


    @Override
    public void speedTestResult(final String result) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {


                if(download == "") {
                    download = result + " Mbit/s";
                    getUploadSpeed();
                } else if (upload == ""){
                    upload = result + " Mbit/s";
                    txtDownload.setText(download);
                    txtUpload.setText(upload);
                    download = "";
                    upload = "";
                    btnStartTest.setEnabled(true);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        txtDownload.setText("...");
        txtUpload.setText("...");

        btnStartTest.setEnabled(false);
        getDownloadSpeed();
    }

    private void getDownloadSpeed() {
        SpeedTest speedtest = new SpeedTest(this);
        speedtest.execute("Down");
    }

    private void getUploadSpeed() {
        SpeedTest speedtest = new SpeedTest(this);
        speedtest.execute("Up");
    }
}

