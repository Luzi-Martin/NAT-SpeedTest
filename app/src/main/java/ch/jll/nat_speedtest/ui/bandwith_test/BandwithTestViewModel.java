package ch.jll.nat_speedtest.ui.bandwith_test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BandwithTestViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BandwithTestViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}