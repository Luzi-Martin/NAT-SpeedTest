package ch.jll.nat_speedtest.ui.mobile_test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ch.jll.nat_speedtest.R;

public class MobileTestFragment extends Fragment {

    private MobileViewModel mobileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mobileViewModel =
                ViewModelProviders.of(this).get(MobileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mobiletest, container, false);
        final TextView textView = root.findViewById(R.id.text_mobileTest);
        mobileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}