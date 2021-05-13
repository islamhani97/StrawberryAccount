package com.islam.strawberryaccount.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.TradersAdapterCallback;
import com.islam.strawberryaccount.databinding.ItemTraderBinding;
import com.islam.strawberryaccount.pojo.Trader;

import java.util.ArrayList;
import java.util.List;

public class TradersAdapter extends RecyclerView.Adapter<TradersAdapter.ItemTraderHolder> {

    private List<Trader> traders;
    private TradersAdapterCallback tradersAdapterCallback;

    public TradersAdapter(TradersAdapterCallback tradersAdapterCallback) {
        traders = new ArrayList<>();
        this.tradersAdapterCallback = tradersAdapterCallback;
    }


    @NonNull
    @Override
    public ItemTraderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemTraderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_trader, parent, false);
        return new ItemTraderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTraderHolder holder, int position) {
        holder.binding.setTrader(traders.get(position));
    }

    @Override
    public int getItemCount() {
        return traders.size();
    }


    public void updateDataSet(List<Trader> traders) {
        this.traders = traders;
        notifyDataSetChanged();
    }


    class ItemTraderHolder extends RecyclerView.ViewHolder {

        ItemTraderBinding binding;

        public ItemTraderHolder(@NonNull ItemTraderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tradersAdapterCallback.onTraderClicked(traders.get(getLayoutPosition()));
                }
            });
        }
    }


}
