package com.islam.strawberryaccount.ui.fragments.home;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.FragmentHomeBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.ui.fragments.traderinfo.TraderInfoFragment;
import com.islam.strawberryaccount.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private Double totalProfit, totalPaid, totalAmount;
    private List<Package> packages;
    private List<Cash> cashList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        totalProfit = totalPaid = totalAmount = 0.0;
        packages = new ArrayList<>();
        cashList = new ArrayList<>();

        homeViewModel.getAllPackages();
        homeViewModel.getAllCashes();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calculateAmountAndProfit();
        calculatePaid();
        observeOnData();
    }

    private void observeOnData() {

        homeViewModel.getPackagesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Package>>() {
            @Override
            public void onChanged(List<Package> packages) {
                HomeFragment.this.packages = packages;
                calculateAmountAndProfit();
            }
        });


        homeViewModel.getCashesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Cash>>() {
            @Override
            public void onChanged(List<Cash> cashList) {
                HomeFragment.this.cashList = cashList;
                calculatePaid();
            }
        });


        homeViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void calculateAmountAndProfit() {

        totalAmount = totalProfit = 0.0;

        for (Package aPackage : packages) {

            totalAmount += aPackage.getAmount();
            totalProfit += (aPackage.getAmount() * aPackage.getPrice());

        }


        binding.fragmentHomeTotalAmount.setText(Constants.suffixText(totalAmount.toString(),getString(R.string.box)));
        binding.fragmentHomeTotalProfit.setText(Constants.suffixText(totalProfit.toString(),getString(R.string.le)));
        double owed = totalProfit - totalPaid;
        binding.fragmentHomeTotalOwed.setText(Constants.suffixText(String.valueOf(owed),getString(R.string.le)));

    }

    private void calculatePaid() {

        totalPaid = 0.0;

        for (Cash cash : cashList) {

            totalPaid += cash.getValue();


        }

        binding.fragmentHomeTotalPaid.setText(Constants.suffixText(totalPaid.toString(),getString(R.string.le)));
        double owed = totalProfit - totalPaid;
        binding.fragmentHomeTotalOwed.setText(Constants.suffixText(String.valueOf(owed),getString(R.string.le)));

    }
}