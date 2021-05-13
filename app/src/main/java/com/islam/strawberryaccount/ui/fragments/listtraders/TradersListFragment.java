package com.islam.strawberryaccount.ui.fragments.listtraders;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.adapters.TradersAdapter;
import com.islam.strawberryaccount.callbacks.TradersAdapterCallback;
import com.islam.strawberryaccount.databinding.FragmentTradersListBinding;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.dialogs.LoadingDialog;
import com.islam.strawberryaccount.ui.dialogs.TraderDialog;
import com.islam.strawberryaccount.utils.Constants;

import java.util.List;


public class TradersListFragment extends Fragment implements TradersAdapterCallback {

    private FragmentTradersListBinding binding;
    private TradersListViewModel tradersListViewModel;
    private TradersAdapter tradersAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradersListViewModel = new ViewModelProvider(this).get(TradersListViewModel.class);
        tradersAdapter = new TradersAdapter(this);
        tradersListViewModel.getAllTraders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_traders_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentTradersListTradersList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentTradersListTradersList.addItemDecoration(Constants.getItemDecoration());
        binding.fragmentTradersListTradersList.setAdapter(tradersAdapter);
        observeOnData();

        binding.fragmentTradersListAddTrader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TraderDialog(getContext(), new Trader(),getString(R.string.save)) {
                    @Override
                    public void onSave(Trader trader) {
                        tradersListViewModel.insertTrader(trader);
                    }
                }.show();
            }
        });


    }


    private void observeOnData() {
        tradersListViewModel.getTradersListLiveData().observe(getViewLifecycleOwner(), new Observer<List<Trader>>() {
            @Override
            public void onChanged(List<Trader> traders) {
                if (traders.size()  < 1) {
                    binding.fragmentTradersListEmptyLabel.setVisibility(View.VISIBLE);
                } else {
                    binding.fragmentTradersListEmptyLabel.setVisibility(View.GONE);
                }
                tradersAdapter.updateDataSet(traders);
            }
        });


        tradersListViewModel.getInsertTraderLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });

        tradersListViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onTraderClicked(Trader trader) {
        NavDirections action = TradersListFragmentDirections.actionTradersListFragmentToTraderFragment(trader.getId());
        NavHostFragment.findNavController(this).navigate(action);
    }
}