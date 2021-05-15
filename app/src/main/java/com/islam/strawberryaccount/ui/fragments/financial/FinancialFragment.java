package com.islam.strawberryaccount.ui.fragments.financial;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.adapters.CashesAdapter;
import com.islam.strawberryaccount.callbacks.CashesAdapterCallback;
import com.islam.strawberryaccount.databinding.FragmentFinancialBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.ui.dialogs.CashDialog;
import com.islam.strawberryaccount.ui.dialogs.ShowCashDialog;
import com.islam.strawberryaccount.utils.Constants;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FinancialFragment extends Fragment implements CashesAdapterCallback {

    private FragmentFinancialBinding binding;
    private FinancialViewModel financialViewModel;
    private Long traderId;
    private CashesAdapter cashesAdapter;
    private Cash updateCash;
    private String language;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traderId = getArguments().getLong(Constants.KEY_TRADER_ID);
        financialViewModel = new ViewModelProvider(this).get(FinancialViewModel.class);
        language = getResources().getConfiguration().locale.getLanguage();
        cashesAdapter = new CashesAdapter(this, language);
        financialViewModel.getAllCashesForTrader(traderId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_financial, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentFinancialList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentFinancialList.addItemDecoration(Constants.getItemDecoration());
        binding.fragmentFinancialList.setAdapter(cashesAdapter);
        observeOnData();

        binding.fragmentFinancialAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CashDialog(getContext(), new Cash(), getString(R.string.save), language) {
                    @Override
                    public void onSave(Cash cash) {
                        cash.setTraderId(traderId);
                        financialViewModel.insertCash(cash);

                    }
                }.show();
            }
        });


    }


    private void observeOnData() {

        financialViewModel.getCashesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Cash>>() {
            @Override
            public void onChanged(List<Cash> cashList) {
                if (cashList.size() < 1) {
                    binding.fragmentFinancialEmptyLabel.setVisibility(View.VISIBLE);
                } else {
                    binding.fragmentFinancialEmptyLabel.setVisibility(View.GONE);
                }
                cashesAdapter.updateDataSet(cashList);


            }
        });


        financialViewModel.getInsertCashLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });

        financialViewModel.getUpdateCashLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                onCashClicked(updateCash);
            }
        });

        financialViewModel.getDeleteCashLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {


            }
        });

        financialViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCashClicked(Cash cash) {
        new ShowCashDialog(getContext(), cash, language) {
            @Override
            public void onEdit(Cash cash) {
                updateCash = cash.getInstance();

                new CashDialog(getContext(), updateCash, getString(R.string.update), language) {
                    @Override
                    public void onSave(Cash cash) {
                        financialViewModel.updateCash(cash);
                    }
                }.show();
            }

            @Override
            public void onDelete(Cash cash) {
                financialViewModel.deleteCash(cash);
            }
        }.show();
    }
}