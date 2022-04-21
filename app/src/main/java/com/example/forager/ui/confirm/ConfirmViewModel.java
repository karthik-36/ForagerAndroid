package com.example.forager.ui.confirm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfirmViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ConfirmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the confirmation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}