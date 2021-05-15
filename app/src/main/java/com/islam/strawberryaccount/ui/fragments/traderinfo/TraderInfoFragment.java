package com.islam.strawberryaccount.ui.fragments.traderinfo;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.databinding.FragmentTraderInfoBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.ui.dialogs.TraderDialog;
import com.islam.strawberryaccount.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TraderInfoFragment extends Fragment {

    private FragmentTraderInfoBinding binding;
    private TraderInfoViewModel traderInfoViewModel;
    private Long traderId;
    private Trader trader;
    private Double totalProfit, totalPaid, totalAmount;
    private List<Package> packages;
    private List<Cash> cashList;

    private Animation rotateOpenAnim, rotateCloseAnim, showingAnim, hidingAnim;
    private boolean active;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        traderInfoViewModel = new ViewModelProvider(this).get(TraderInfoViewModel.class);
        traderId = getArguments().getLong(Constants.KEY_TRADER_ID);

        totalProfit = totalPaid = totalAmount = 0.0;
        packages = new ArrayList<>();
        cashList = new ArrayList<>();

        traderInfoViewModel.getTrader(traderId);
        traderInfoViewModel.getAllPackagesForTrader(traderId);
        traderInfoViewModel.getAllCashesForTrader(traderId);

        rotateOpenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_rotate_open);
        rotateCloseAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_rotate_close);
        showingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_show_top);
        hidingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_hide_top);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trader_info, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        active = false;

        calculateAmountAndProfit();
        calculatePaid();
        observeOnData();


        binding.fragmentTraderInfoFloatingActions.actionsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configAction();
            }
        });


        binding.fragmentTraderInfoFloatingActions.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (trader != null) {
                    new TraderDialog(getContext(), trader,getString(R.string.update)) {
                        @Override
                        public void onSave(Trader trader) {
                            traderInfoViewModel.updateTrader(trader);
                        }
                    }.show();
                }
            }
        });

        binding.fragmentTraderInfoFloatingActions.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trader != null) {
                    new MaterialAlertDialogBuilder(getContext(), R.style.DialogTheme)
                            .setMessage(R.string.message_delete)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    traderInfoViewModel.deleteTrader(trader);
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .setCancelable(false)
                            .setBackground(getContext().getDrawable(R.drawable.shape_dialog_background))
                            .show();

                }
            }
        });

    }

    private void observeOnData() {
        traderInfoViewModel.getTraderLiveData().observe(getViewLifecycleOwner(), new Observer<Trader>() {
            @Override
            public void onChanged(Trader trader) {
                TraderInfoFragment.this.trader = trader;
                binding.fragmentTraderInfoTraderName.setText(getString(R.string.trader_name)+":  "+trader.getName());

            }
        });


        traderInfoViewModel.getPackagesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Package>>() {
            @Override
            public void onChanged(List<Package> packages) {
                TraderInfoFragment.this.packages = packages;
                calculateAmountAndProfit();

            }
        });


        traderInfoViewModel.getCashesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Cash>>() {
            @Override
            public void onChanged(List<Cash> cashList) {
                TraderInfoFragment.this.cashList = cashList;
                calculatePaid();

            }
        });

        traderInfoViewModel.getUpdateTraderLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

            }
        });


        traderInfoViewModel.getDeleteTraderLiveData().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                NavHostFragment.findNavController(TraderInfoFragment.this).popBackStack();
            }
        });
        traderInfoViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
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



        binding.fragmentTraderInfoTotalAmount.setText(Constants.suffixText(totalAmount.toString(),getString(R.string.box)));
        binding.fragmentTraderInfoTotalProfit.setText(Constants.suffixText(totalProfit.toString(),getString(R.string.le)));
        double owed = totalProfit - totalPaid;
        binding.fragmentTraderInfoTotalOwed.setText(Constants.suffixText(String.valueOf(owed),getString(R.string.le)));

    }

    private void calculatePaid() {

        totalPaid = 0.0;

        for (Cash cash : cashList) {

            totalPaid += cash.getValue();


        }

        binding.fragmentTraderInfoTotalPaid.setText(Constants.suffixText(totalPaid.toString(),getString(R.string.le)));
        double owed = totalProfit - totalPaid;
        binding.fragmentTraderInfoTotalOwed.setText(Constants.suffixText(String.valueOf(owed),getString(R.string.le)));

    }




    private void configAction() {
        active = !active;
        setVisibility(active);
        setAnimation(active);
        setClickable(active);
    }

    private void setAnimation(boolean active) {

        if (active) {
            binding.fragmentTraderInfoFloatingActions.actionsGroup.startAnimation(rotateOpenAnim);
            binding.fragmentTraderInfoFloatingActions.actionEdit.startAnimation(showingAnim);
            binding.fragmentTraderInfoFloatingActions.actionDelete.startAnimation(showingAnim);

        } else {
            binding.fragmentTraderInfoFloatingActions.actionsGroup.startAnimation(rotateCloseAnim);
            binding.fragmentTraderInfoFloatingActions.actionEdit.startAnimation(hidingAnim);
            binding.fragmentTraderInfoFloatingActions.actionDelete.startAnimation(hidingAnim);

        }

    }

    private void setVisibility(boolean active) {
        if (active) {
            binding.fragmentTraderInfoFloatingActions.actionEdit.setVisibility(View.VISIBLE);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setVisibility(View.VISIBLE);
        } else {

            binding.fragmentTraderInfoFloatingActions.actionEdit.setVisibility(View.INVISIBLE);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setVisibility(View.INVISIBLE);
        }


    }

    private void setClickable(boolean active) {

        if (active) {
            binding.fragmentTraderInfoFloatingActions.actionEdit.setClickable(true);
            binding.fragmentTraderInfoFloatingActions.actionEdit.setFocusable(true);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setClickable(true);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setFocusable(true);
        } else {

            binding.fragmentTraderInfoFloatingActions.actionEdit.setClickable(false);
            binding.fragmentTraderInfoFloatingActions.actionEdit.setFocusable(false);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setClickable(false);
            binding.fragmentTraderInfoFloatingActions.actionDelete.setFocusable(false);
        }
    }


}