package com.islam.strawberryaccount.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.CashesAdapterCallback;
import com.islam.strawberryaccount.callbacks.TradersAdapterCallback;
import com.islam.strawberryaccount.databinding.ItemCashBinding;
import com.islam.strawberryaccount.databinding.ItemTraderBinding;
import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Trader;
import com.islam.strawberryaccount.utils.Constants;
import com.islam.strawberryaccount.utils.DateConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashesAdapter extends RecyclerView.Adapter<CashesAdapter.ItemCashHolder> {

    private List<Cash> cashList;
    private CashesAdapterCallback cashesAdapterCallback;
    private String language ;

    public CashesAdapter(CashesAdapterCallback cashesAdapterCallback,String language) {
        cashList = new ArrayList<>();
        this.cashesAdapterCallback = cashesAdapterCallback;
        this.language = language ;
    }

    @NonNull
    @Override
    public ItemCashHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCashBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_cash, parent, false);
        return new ItemCashHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCashHolder holder, int position) {
        holder.binding.setLanguage(language);
        holder.binding.setCash(cashList.get(position));
    }

    @Override
    public int getItemCount() {
        return cashList.size();
    }

    public void updateDataSet(List<Cash> cashList) {
        this.cashList = cashList;
        Collections.sort( this.cashList , Constants.CASH_COMPARATOR);
        notifyDataSetChanged();
    }


    class ItemCashHolder extends RecyclerView.ViewHolder {

        ItemCashBinding binding;

        public ItemCashHolder(@NonNull ItemCashBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cashesAdapterCallback.onCashClicked(cashList.get(getLayoutPosition()));
                }
            });
        }
    }


}
