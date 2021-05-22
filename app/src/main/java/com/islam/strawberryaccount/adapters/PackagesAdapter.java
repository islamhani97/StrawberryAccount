package com.islam.strawberryaccount.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.strawberryaccount.R;
import com.islam.strawberryaccount.callbacks.PackagesAdapterCallback;
import com.islam.strawberryaccount.databinding.ItemPackageBinding;
import com.islam.strawberryaccount.pojo.Package;
import com.islam.strawberryaccount.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ItemPackageHolder> {

    private List<Package> packages;
    private PackagesAdapterCallback packagesAdapterCallback;
    private String language;

    public PackagesAdapter(PackagesAdapterCallback packagesAdapterCallback, String language) {
        packages = new ArrayList<>();
        this.packagesAdapterCallback = packagesAdapterCallback;
        this.language = language;
    }


    @NonNull
    @Override
    public ItemPackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPackageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_package, parent, false);
        return new ItemPackageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPackageHolder holder, int position) {
        holder.binding.setLanguage(language);
        holder.binding.setAPackage(packages.get(position));

    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public void updateDataSet(List<Package> packages) {
        this.packages = packages;
        Collections.sort(this.packages, Constants.PACKAGES_COMPARATOR);
        notifyDataSetChanged();
    }


    class ItemPackageHolder extends RecyclerView.ViewHolder {

        ItemPackageBinding binding;

        public ItemPackageHolder(@NonNull ItemPackageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    packagesAdapterCallback.onPackageClicked(packages.get(getLayoutPosition()));


                }
            });
        }
    }


}
