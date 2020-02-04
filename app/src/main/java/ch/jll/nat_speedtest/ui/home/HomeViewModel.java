package ch.jll.nat_speedtest.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Willkommen auf NAT-Speedtest");
    }

    public LiveData<String> getText() {
        return mText;
    }
}