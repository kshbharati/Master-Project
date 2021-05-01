package com.federation.masters.preuni.parentHome.ui.grades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GradesViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GradesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is grades fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
