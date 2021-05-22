package com.islam.strawberryaccount.ui.fragments.trader;

import android.os.Bundle;
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
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.adapters.TraderPagesAdapter;
import com.islam.strawberryaccount.databinding.FragmentTraderBinding;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.activities.main.MainActivity;
import com.islam.strawberryaccount.ui.fragments.financial.FinancialFragment;
import com.islam.strawberryaccount.ui.fragments.sales.SalesFragment;
import com.islam.strawberryaccount.ui.fragments.traderinfo.TraderInfoFragment;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TraderFragment extends Fragment {

    private FragmentTraderBinding binding;
    private TraderViewModel traderViewModel;
    private TraderPagesAdapter traderPagesAdapter;
    private Long traderId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traderViewModel = new ViewModelProvider(this).get(TraderViewModel.class);
        traderId = TraderFragmentArgs.fromBundle(getArguments()).getTraderId();

        traderViewModel.getTrader(traderId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trader, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeOnDAta();

        traderPagesAdapter = new TraderPagesAdapter(this, getPages());
        binding.fragmentTraderViewPager.setOffscreenPageLimit(3);
        binding.fragmentTraderViewPager.setAdapter(traderPagesAdapter);

        new TabLayoutMediator(binding.fragmentTraderTabs, binding.fragmentTraderViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position) {

                    case 0: {
                        tab.setText(R.string.trader_info);
                        break;
                    }
                    case 1: {
                        tab.setText(R.string.sales);
                        break;
                    }
                    case 2: {
                        tab.setText(R.string.financial);
                        break;
                    }
                }

            }
        }).attach();

        binding.fragmentTraderViewPager.setPageTransformer(new DepthPageTransformer());

        ((MainActivity) getActivity()).getSearchView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = TraderFragmentDirections.actionTraderFragmentToSearchFragment(traderId);
                NavHostFragment.findNavController(TraderFragment.this).navigate(action);

            }
        });
    }


    private void observeOnDAta() {
        traderViewModel.getTraderLiveData().observe(getViewLifecycleOwner(), new Observer<Trader>() {
            @Override
            public void onChanged(Trader trader) {
                ((MainActivity) getActivity()).getSupportActionBar().setTitle(trader.getName());

            }
        });
        traderViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Fragment> getPages() {
        List<Fragment> fragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_TRADER_ID, traderId);

        TraderInfoFragment traderInfoFragment = new TraderInfoFragment();
        traderInfoFragment.setArguments(bundle);
        fragments.add(traderInfoFragment);

        SalesFragment salesFragment = new SalesFragment();
        salesFragment.setArguments(bundle);
        fragments.add(salesFragment);

        FinancialFragment financialFragment = new FinancialFragment();
        financialFragment.setArguments(bundle);
        fragments.add(financialFragment);

        return fragments;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).getSearchView().setOnClickListener(null);
    }
}