package com.islam.strawberryaccount.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TraderPagesAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    public TraderPagesAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

//    public TraderPagesAdapter(@NonNull FragmentManager fm, List<Page> pages) {
//        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        this.pages = pages;
//
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return pages.get(position).getFragment();
//    }
//
//    @Override
//    public int getCount() {
//        return pages.size();
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return pages.get(position).getTitle();
//    }
}
