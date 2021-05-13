package com.islam.strawberryaccount.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.SignInCallback;
import com.islam.strawberryaccount.databinding.FragmentAuthCodeBinding;
import com.islam.strawberryaccount.views.VerificationCodeEditText;


public class AuthCodeFragment extends Fragment {

    private FragmentAuthCodeBinding binding;
    private SignInCallback signInCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_code, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.fragmentAuthCodeAuthCode.setOnCodeCompleteListener(new VerificationCodeEditText.OnCodeCompleteListener() {
            @Override
            public void onCodeComplete(String code) {
                if (!code.isEmpty()) {
                    signInCallback.onCodeSubmit(code);
                }
            }
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        signInCallback = (SignInCallback) context;
    }
}
