package ch.jll.nat_speedtest.ui.bandwith_test;

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

import org.json.JSONObject;

import ch.jll.nat_speedtest.R;
import ch.jll.nat_speedtest.speedtest.BandWithTest;


public class BandwithTestFragment extends Fragment implements View.OnClickListener{

    private BandwithTestViewModel bandwithTestViewModel;
    private Button btnStartTest;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bandwithTestViewModel =
                ViewModelProviders.of(this).get(BandwithTestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bandwithtest, container, false);
        final TextView textView = root.findViewById(R.id.text_bandWithTest);
        bandwithTestViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        btnStartTest = root.findViewById(R.id.btnStartBandWithTest);
        btnStartTest.setOnClickListener(this);
        return root;
    }


    @Override
    public void onClick(View v) {
        BandWithTest bandWithTest = new BandWithTest();
        bandWithTest.execute();
    }
}