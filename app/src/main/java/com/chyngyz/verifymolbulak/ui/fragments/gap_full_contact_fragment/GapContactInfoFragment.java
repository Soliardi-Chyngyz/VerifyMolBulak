package com.chyngyz.verifymolbulak.ui.fragments.gap_full_contact_fragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.chyngyz.verifymolbulak.R;
import com.chyngyz.verifymolbulak.databinding.GapContactInfoFragmentBinding;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class GapContactInfoFragment extends Fragment {

    private GapContactInfoFragmentBinding binding;
    private GapContactInfoViewModel mViewModel;
    private String selectedQuestion;
    private boolean isAgreed = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GapContactInfoFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GapContactInfoViewModel.class);

        adapterInit();

        validation();

        getDataForResult();

        onViewClick();
    }

    private void getDataForResult() {
        mViewModel.getPasswordInfo().observe(requireActivity(), aBoolean -> {
            binding.regEditPassword.setError("Должны быть буквы и номера");
            binding.regEditPasswordConfirm.setError("Должны быть буквы и номера");
            mViewModel.wrongPassword.setValue(false);
        });

    }

    private void adapterInit() {
        Resources res = getActivity().getResources();
        String[] answers = res.getStringArray(R.array.answers);
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.custom_textview_spinner, answers);
        binding.regEditChooseQuestion.setAdapter(adapter);
        binding.regEditChooseQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Выберите секретный вопрос")) {
                    parent.getItemAtPosition(1);
                } else {
//                    selectedQuestion = parent.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void validation() {
        assert binding.regBtnRegistration != null;
        binding.regBtnRegistration.setOnClickListener(v -> {
            String name = binding.regEditName.getText().toString();
            String phoneNumber = binding.regEditPhoneNumber.getText().toString();
            String password = binding.regEditPassword.getText().toString();
            String passwordConfirm = binding.regEditPasswordConfirm.getText().toString();
            showPassword(binding.regShowPassword, binding.regEditPassword);
            showPassword(binding.regEditShowPassword2, binding.regEditPasswordConfirm);
            String selectedAnswer = binding.regEditAnswer.getText().toString();
            if (name.length() >= 3 && phoneNumber.length() == 13 && password.length() >= 6
                    && passwordConfirm.length() >= 6 && !selectedAnswer.isEmpty() && isAgreed) {
                mViewModel.setValidation(name, phoneNumber, password, passwordConfirm, selectedAnswer,
                        isAgreed);
            } else if (name.length() < 3) {
                yoyo(binding.regEditName);
                binding.regEditName.setError("Не так коротко");
            } else if (phoneNumber.length() != 13) {
                yoyo(binding.regEditPhoneNumber);
                binding.regEditPhoneNumber.setError("Проверьте номер");
            } else if (password.length() != passwordConfirm.length()) {
                yoyo(binding.regEditPasswordConfirm);
                binding.regEditPasswordConfirm.setError("Не совпадает");
            } else if(passwordConfirm.isEmpty()){
                binding.regEditPasswordConfirm.setError("Должны быть буквы и цифры");
            }
            else if (password.isEmpty()){
                binding.regEditPassword.setError("Заполните поле");
                yoyo(binding.regEditPassword);
            } else if (selectedAnswer.isEmpty()) {
                yoyo(binding.regEditAnswer);
                binding.regEditAnswer.setError("Заполните поле");
            } else if (!isAgreed){
                Toast.makeText(requireActivity(), "Подтвердите соглашение", Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getInfo().observe(requireActivity(), aBoolean -> {
            if(aBoolean){
                Toast.makeText(requireActivity(), "Succedd", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_gapContactInfoFragment_to_succedFragment);
            }
        });
    }

    private void yoyo(View view) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(view);
    }

    private void onViewClick() {
        assert binding.viewConfirm != null;
        binding.viewConfirm.setOnClickListener(v -> {
            binding.viewConfirm.setBackgroundColor(Color.GREEN);
            isAgreed = true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPassword(TextView regShowPassword, EditText regEditPassword) {
        regShowPassword.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    regEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;

                case MotionEvent.ACTION_DOWN:
                    regEditPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
            return true;
        });
    }
}
