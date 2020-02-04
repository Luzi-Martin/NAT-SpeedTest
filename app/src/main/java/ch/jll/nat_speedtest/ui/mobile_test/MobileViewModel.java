package ch.jll.nat_speedtest.ui.mobile_test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MobileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MobileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Mobile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}