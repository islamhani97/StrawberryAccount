package com.islam.strawberryaccount.ui.fragments.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
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
import com.islam.strawberryaccount.adapters.PackagesAdapter;
import com.islam.strawberryaccount.callbacks.CashesAdapterCallback;
import com.islam.strawberryaccount.callbacks.PackagesAdapterCallback;
import com.islam.strawberryaccount.databinding.FragmentSearchBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.ui.dialogs.CashDialog;
import com.islam.strawberryaccount.ui.dialogs.PackageDialog;
import com.islam.strawberryaccount.ui.dialogs.ShowCashDialog;
import com.islam.strawberryaccount.ui.dialogs.ShowPackageDialog;
import com.islam.strawberryaccount.utils.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment implements PackagesAdapterCallback, CashesAdapterCallback {

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private Long traderId;

    private PackagesAdapter packagesAdapter;
    private CashesAdapter cashesAdapter;

    private Package updatePackage;
    private Cash updateCash;
    private String language;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        traderId = SearchFragmentArgs.fromBundle(getArguments()).getTraderId();

        language = getResources().getConfiguration().locale.getLanguage();
        packagesAdapter = new PackagesAdapter(this, language);
        cashesAdapter = new CashesAdapter(this, language);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showData();

        binding.fragmentSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.fragmentSearchList.addItemDecoration(Constants.getItemDecoration());
        observeOnData();

        binding.fragmentSearchDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar startCalendar = Calendar.getInstance();
                        Calendar endCalendar = Calendar.getInstance();

                        startCalendar.set(year, month, dayOfMonth, 0, 0, 0);
                        endCalendar.set(year, month, dayOfMonth, 23, 59, 59);

                        searchViewModel.setStartSearchDate(startCalendar.getTime().getTime());
                        searchViewModel.setEndSearchDate(endCalendar.getTime().getTime());
                        showData();

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        binding.fragmentSearchTypeGroup.setOnCheckedChangeListener(searchTypeListener);
        binding.fragmentSearchSearchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isErrorExist()) {
                    search();
                }
            }
        });


    }

    private void search() {

        if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_SALES)) {
            searchViewModel.searchInPackagesForTrader(traderId);
        } else if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_FINANCIAL)) {
            searchViewModel.searchInCashesForTrader(traderId);

        }
    }

    private void showData() {

        if (searchViewModel.getStartSearchDate() != null) {
            binding.fragmentSearchDate.getEditText().setText(Constants.showDate(new Date(searchViewModel.getStartSearchDate()), language));
        }
        if (searchViewModel.getSearchType() != null) {
            binding.fragmentSearchTypeGroup.setOnCheckedChangeListener(null);
            if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_SALES)) {

                binding.fragmentSearchTypeSales.setChecked(true);
            } else if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_FINANCIAL)) {

                binding.fragmentSearchTypeFinancial.setChecked(true);

            }
            binding.fragmentSearchTypeGroup.setOnCheckedChangeListener(searchTypeListener);

        }


    }

    private boolean isErrorExist() {

        boolean error = false;


        if (searchViewModel.getStartSearchDate() == null || searchViewModel.getEndSearchDate() == null) {
            Toast.makeText(getContext(), getString(R.string.error_select_date), Toast.LENGTH_SHORT).show();
            error = true;
        }

        if (searchViewModel.getSearchType() == null) {
            Toast.makeText(getContext(), getString(R.string.error_select_search_type), Toast.LENGTH_SHORT).show();
            error = true;
        }


        return error;
    }

    private void observeOnData() {


        searchViewModel.getPackagesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Package>>() {
            @Override
            public void onChanged(List<Package> packages) {
                if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_SALES)) {

                    if (packages.size() < 1) {
                        binding.fragmentSearchNorResultLabel.setVisibility(View.VISIBLE);
                    } else {
                        binding.fragmentSearchNorResultLabel.setVisibility(View.GONE);
                    }


                    binding.fragmentSearchList.setAdapter(packagesAdapter);
                    packagesAdapter.updateDataSet(packages);
                }
            }
        });


        searchViewModel.getCashesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Cash>>() {
            @Override
            public void onChanged(List<Cash> cashList) {
                if (searchViewModel.getSearchType().equals(Constants.SEARCH_TYPE_FINANCIAL)) {

                    if (cashList.size() < 1) {
                        binding.fragmentSearchNorResultLabel.setVisibility(View.VISIBLE);
                    } else {
                        binding.fragmentSearchNorResultLabel.setVisibility(View.GONE);
                    }


                    binding.fragmentSearchList.setAdapter(cashesAdapter);
                    cashesAdapter.updateDataSet(cashList);

                }
            }
        });

        searchViewModel.getUpdatePackageLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                search();
                onPackageClicked(updatePackage);
            }
        });

        searchViewModel.getDeletePackageLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                search();
            }
        });

        searchViewModel.getUpdateCashLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                search();
                onCashClicked(updateCash);
            }
        });

        searchViewModel.getDeleteCashLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                search();
            }
        });


        searchViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private final RadioGroup.OnCheckedChangeListener searchTypeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.fragment_search_type_sales: {
                    searchViewModel.setSearchType(Constants.SEARCH_TYPE_SALES);
                    break;
                }
                case R.id.fragment_search_type_financial: {
                    searchViewModel.setSearchType(Constants.SEARCH_TYPE_FINANCIAL);
                    break;
                }
            }

            showData();
        }
    };

    @Override
    public void onPackageClicked(Package aPackage) {
        new ShowPackageDialog(getContext(), aPackage, language) {
            @Override
            public void onEdit(Package aPackage) {
                updatePackage = aPackage.getInstance();
                new PackageDialog(getContext(), updatePackage, getString(R.string.update), language) {
                    @Override
                    public void onSave(Package aPackage) {
                        searchViewModel.updatePackage(aPackage);
                    }
                }.show();

            }

            @Override
            public void onDelete(Package aPackage) {

                searchViewModel.deletePackage(aPackage);

            }
        }.show();
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
                        searchViewModel.updateCash(cash);
                    }
                }.show();
            }

            @Override
            public void onDelete(Cash cash) {
                searchViewModel.deleteCash(cash);
            }
        }.show();
    }
}