package com.chyngyz.verifymolbulak.ui.fragments.gap_full_contact_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.chyngyz.verifymolbulak.core.Constants.PASSWORD_PATTERN;

public class GapContactInfoViewModel extends ViewModel {
    public MutableLiveData<Boolean> wrongPassword = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public LiveData<Boolean> getPasswordInfo() {
        return wrongPassword;
    }

    public LiveData<Boolean> getInfo() {
        return isSuccess;
    }

    public void setValidation(String name, String phoneNumber, String password, String passwordConfirm,
                              String answerSpin, boolean clientAgree) {
        if (password.equals(passwordConfirm) && isValidPassword(password, passwordConfirm))
            isSuccess.setValue(true);
        else if (!isValidPassword(password, passwordConfirm))
            wrongPassword.setValue(true);
    }

    private boolean isValidPassword(String password, String pass2) {
        if (!PASSWORD_PATTERN.matcher(password).matches() && !PASSWORD_PATTERN.matcher(pass2).matches()) {
            return true;
        } else {
            wrongPassword.setValue(true);
            return false;
        }
    }
}
