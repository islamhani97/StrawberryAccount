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
import com.islam.strawberryaccount.databinding.FragmentPhoneNumberBinding;
import com.islam.strawberryaccount.utils.Constants;


public class PhoneNumberFragment extends Fragment {

    private FragmentPhoneNumberBinding binding;
    private SignInCallback signInCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentPhoneNumberNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.fragmentPhoneNumberPhoneNumber.getText().toString();
                if (phoneNumber.isEmpty()) {
                    binding.fragmentPhoneNumberPhoneNumber.setError(getString(R.string.error_phone_number_required));
                } else if (!phoneNumber.matches(Constants.PHONE_PATTERN)) {
                    binding.fragmentPhoneNumberPhoneNumber.setError(getString(R.string.error_phone_number));
                } else {
                    signInCallback.verifyPhoneNumber("+2" + phoneNumber);
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
