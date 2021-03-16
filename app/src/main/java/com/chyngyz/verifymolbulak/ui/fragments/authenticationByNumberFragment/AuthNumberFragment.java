package com.chyngyz.verifymolbulak.ui.fragments.authenticationByNumberFragment;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chyngyz.verifymolbulak.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class AuthNumberFragment extends Fragment {

    /**
     * Firebase Auth конфликтует с binding поэтому я инициализировал view по старинке
     */

    private LinearLayout card1;
    private CardView card2;
    private EditText editPhoneNumber, editSms;
    private Button btnNext, btnConfirm;
    private TextView txtRepeat, txtInfoTime, skip;
    private ImageView imgCross;

    private AuthNumberViewModel vModel;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 60000;
    private String phoneNumber;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_number_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vModel = new ViewModelProvider(this).get(AuthNumberViewModel.class);

        initView(view);

        initCallBacks();

        onBackClicked();

        onBtnSend();

        onBtnConfirm();

        onCrossClick();
    }

    private void onCrossClick() {
        imgCross.setOnClickListener(v -> {
            editSms.setText("");
            card2.setVisibility(View.GONE);
            card1.setVisibility(View.VISIBLE);
        });
        skip.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_authNumberFragment_to_gapContactInfoFragment);
        });
    }

    private void initView(View view) {
        txtRepeat = view.findViewById(R.id.auth_btn_repeat);
        editPhoneNumber = view.findViewById(R.id.auth_edit_phone_number);
        editSms = view.findViewById(R.id.auth_edit_sms);
        btnNext = view.findViewById(R.id.auth_btn_of_next);
        btnConfirm = view.findViewById(R.id.auth_btn_confirm);
        card1 = view.findViewById(R.id.auth_card_1);
        card2 = view.findViewById(R.id.auth_card_2);
        txtInfoTime = view.findViewById(R.id.auth_info_second);
        imgCross = view.findViewById(R.id.auth_cross_img);
        skip = view.findViewById(R.id.btn_skip);
    }

    private void onBackClicked() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    private void onBtnConfirm() {
        btnConfirm.setOnClickListener(v -> {
            if (!editSms.getText().toString().trim().isEmpty()) {
                try {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId, editSms.getText().toString().trim());
                    Toast.makeText(requireContext(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                    signIn(phoneAuthCredential);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(requireContext(), "Скорее всего вы ввели неверный код, либо он не актуален, попытайтесь позже", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onBtnSend() {
        btnNext.setOnClickListener(v -> requestSms());
    }

    private void requestSms() {
        phoneNumber = editPhoneNumber.getText().toString().trim();
        if (phoneNumber.length() == 13) {
            smsSend(phoneNumber);
        } else if (phoneNumber.isEmpty()) {
            yoyo(editPhoneNumber);
            editPhoneNumber.setError("Это поле не должно быть пустым");
        } else {
            yoyo(editPhoneNumber);
            editPhoneNumber.setError("Проверьте пожалуйста введенный номер");
        }
    }

    private void yoyo(View view) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(view);
    }

    private void smsSend(String phoneNumber) {
        PhoneAuthOptions option = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(option);
        startTimer();
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.VISIBLE);
    }

    private void startTimer() {
        timeLeftInMilliSeconds = 60000;
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSeconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(requireActivity(), "Если смс-код не пришел, проверьте соединение, номер и повторите заново", Toast.LENGTH_LONG).show();
                txtRepeat.setOnClickListener(v -> {
                    card1.setVisibility(View.VISIBLE);
                    card2.setVisibility(View.GONE);
                });
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void updateTimer() {
        int seconds = (int) (timeLeftInMilliSeconds % 60000 / 1000);
        txtInfoTime.setText(String.valueOf(seconds) + " секунд");
    }

    private void initCallBacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
                String smsCode = phoneAuthCredential.getSmsCode();
                if (smsCode != null) {
                    editSms.setText(smsCode);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                Toast.makeText(getActivity(), "Код отправлен!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        // создаем и авторизируемся
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                countDownTimer.cancel();
                Toast.makeText(requireContext(), "Добро пожаловать", Toast.LENGTH_LONG).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_authNumberFragment_to_gapContactInfoFragment);
            } else {
                Toast.makeText(requireContext(), "Проверьте соединение интернета", Toast.LENGTH_LONG).show();
            }
        });
    }
}