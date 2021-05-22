package com.islam.strawberryaccount.ui.fragments.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.SettingsCallback;
import com.islam.strawberryaccount.databinding.FragmentSettingsBinding;
import com.islam.strawberryaccount.utils.Constants;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsViewModel;
    private String language;
    private String selectedLanguage;
    private SettingsCallback settingsCallback;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.getLanguage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsViewModel.getLanguageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                language = s;
                if (s.equals(Constants.LANGUAGE_EN)) {
                    binding.fragmentSettingsLanguage.setText(R.string.language_en);
                } else if (s.equals(Constants.LANGUAGE_AR)) {
                    binding.fragmentSettingsLanguage.setText(R.string.language_ar);
                }
            }
        });


        binding.fragmentSettingsEditLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s[] = {getString(R.string.language_en), getString(R.string.language_ar)};
                selectedLanguage = language;
                new MaterialAlertDialogBuilder(getContext(), R.style.DialogTheme)
                        .setTitle(R.string.message_choose_language)
                        .setSingleChoiceItems(s,
                                selectedLanguage.equals(Constants.LANGUAGE_EN) ? 0 : 1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0: {
                                                selectedLanguage = Constants.LANGUAGE_EN;
                                                break;
                                            }
                                            case 1: {
                                                selectedLanguage = Constants.LANGUAGE_AR;
                                                break;
                                            }

                                        }
                                    }
                                })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setLocale(selectedLanguage);
                                settingsViewModel.setLanguage(selectedLanguage);
                                settingsCallback.refreshActivity();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .setCancelable(false)
                        .setBackground(getContext().getDrawable(R.drawable.shape_dialog_background))
                        .show();
            }
        });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        settingsCallback = (SettingsCallback) context;
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);

    }

}