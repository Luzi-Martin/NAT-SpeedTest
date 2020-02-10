package ch.jll.nat_speedtest.ui.bandwith_test;
import ch.jll.nat_speedtest.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;

public class BandwithTestFragment extends Fragment {

    private BandwithTestViewModel bandwithTestViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bandwithTestViewModel =
                ViewModelProviders.of(this).get(BandwithTestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bandwithtest, container, false);
        final TextView textView = root.findViewById(R.id.text_bandWithTest);
        final Button button = root.findViewById(R.id.btnStartTest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        bandwithTestViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }



    private void sendData() throws IOException {
        String postalCode, place, street, houseNumber;
        View view = getView();

        EditText postalCodeEdit = getView().findViewById(R.id.inputPlz);
        EditText placeEdit = view.findViewById(R.id.inputPlace);
        EditText streetEdit = view.findViewById(R.id.inputStreet);
        EditText numberEdit = view.findViewById(R.id.inputHouseNumber);

        postalCode = postalCodeEdit.getText().toString();
        place = placeEdit.getText().toString();
        street = streetEdit.getText().toString();
        houseNumber = numberEdit.getText().toString();

        if(!postalCode.equals("") && !place.equals("") && !street.equals("") && !houseNumber.equals(""))  {
            String jsonInputString = String.format("{ \"address\": { \"locationId\": \"\", \"zipCode4\": \"%s\", \"city\": \"%s\", \"street\": \"%s\", \"houseNumber\": \"%s\"}, \"language\": \"de\" }", postalCode, place, street, houseNumber);
        }
    }
}