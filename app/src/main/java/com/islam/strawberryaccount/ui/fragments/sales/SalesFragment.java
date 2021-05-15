package com.islam.strawberryaccount.ui.fragments.sales;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.adapters.PackagesAdapter;
import com.islam.strawberryaccount.callbacks.PackagesAdapterCallback;
import com.islam.strawberryaccount.databinding.FragmentSalesBinding;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.dialogs.PackageDialog;
import com.islam.strawberryaccount.ui.dialogs.ShowPackageDialog;
import com.islam.strawberryaccount.ui.dialogs.TraderDialog;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.DateConverter;

import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SalesFragment extends Fragment implements PackagesAdapterCallback {
    private FragmentSalesBinding binding;
    private SalesViewModel salesViewModel;
    private Long traderId;
    private PackagesAdapter packagesAdapter;
    private Package updatePackage;
    private String language;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traderId = getArguments().getLong(Constants.KEY_TRADER_ID);
        salesViewModel = new ViewModelProvider(this).get(SalesViewModel.class);
        language = getResources().getConfiguration().locale.getLanguage();
        packagesAdapter = new PackagesAdapter(this,language);
        salesViewModel.getAllPackagesForTrader(traderId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sales, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.fragmentSalesList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentSalesList.addItemDecoration(Constants.getItemDecoration());
        binding.fragmentSalesList.setAdapter(packagesAdapter);

        observeOnData();

        binding.fragmentSalesAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PackageDialog(getContext(), new Package(), getString(R.string.save),language) {
                    @Override
                    public void onSave(Package aPackage) {
                        aPackage.setTraderId(traderId);
                        salesViewModel.insertPackage(aPackage);

                    }
                }.show();
            }
        });


    }

    private void observeOnData() {

        salesViewModel.getPackagesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Package>>() {
            @Override
            public void onChanged(List<Package> packages) {
                if(packages.size()  < 1 ){
                    binding.fragmentSalesEmptyLabel.setVisibility(View.VISIBLE);
                }
                else{
                    binding.fragmentSalesEmptyLabel.setVisibility(View.GONE);
                }
                packagesAdapter.updateDataSet(packages);


            }
        });

        salesViewModel.getInsertPackageLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });

        salesViewModel.getUpdatePackageLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                onPackageClicked(updatePackage);
            }
        });

        salesViewModel.getDeletePackageLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });

        salesViewModel.getErrorLiveDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPackageClicked(Package aPackage) {
        new ShowPackageDialog(getContext(), aPackage,language) {
            @Override
            public void onEdit(Package aPackage) {
                updatePackage = aPackage.getInstance();
                new PackageDialog(getContext(), updatePackage, getString(R.string.update),language) {
                    @Override
                    public void onSave(Package aPackage) {
                        salesViewModel.updatePackage(aPackage);
                    }
                }.show();

            }

            @Override
            public void onDelete(Package aPackage) {

                salesViewModel.deletePackage(aPackage);

            }
        }.show();
    }
}