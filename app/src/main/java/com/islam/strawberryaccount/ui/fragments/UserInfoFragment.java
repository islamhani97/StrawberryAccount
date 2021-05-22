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
import com.islam.strawberryaccount.databinding.FragmentUserInfoBinding;
import com.islam.strawberryaccount.pojo.User;


public class UserInfoFragment extends Fragment {

    private FragmentUserInfoBinding binding;
    private SignInCallback signInCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentUserInfoName.setText(UserInfoFragmentArgs.fromBundle(getArguments()).getUsername());

        binding.fragmentUserInfoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.fragmentUserInfoName.getText().toString().trim();
                if (!name.isEmpty() && name.length() > 2) {
                    User user = new User();
                    user.setName(name);
                    signInCallback.submitUserInfo(user);
                } else if (name.isEmpty()) {
                    binding.fragmentUserInfoName.setError(getString(R.string.error_username_required));
                } else {
                    binding.fragmentUserInfoName.setError(getString(R.string.error_name_length));
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
