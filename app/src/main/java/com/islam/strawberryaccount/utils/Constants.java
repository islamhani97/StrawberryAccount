package com.islam.strawberryaccount.utils;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.strawberryaccount.pojo.Cash;
import com.islam.strawberryaccount.pojo.Package;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Constants {

    public static final String PHONE_PATTERN = "(010|011|012|015)+[0-9]{8}";

    // Shared Preferences
    public static final String SH_PREFS_NAME = "StrawberryAccountShPrefs";
    public static final String SH_PREFS_SIGN_IN = "SignIn";
    public static final String SH_PREFS_LANGUAGE = "Language";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_AR = "ar";
    public static final String NONE = "none";

    // Firebase Keys
    public static final String COLLECTION_USERS = "Users";
    public static final String COLLECTION_VERSION = "Version";
    public static final String DOCUMENT_VERSION = "Version";

    // Fields Keys
    public static final String KEY_COMPLETABLE = "Completable";
    public static final String KEY_SHOW_ABOUT = "ShowAbout";
    public static final String KEY_TRADER_ID = "traderId";

    // search Types
    public static final String SEARCH_TYPE_SALES = "Sales";
    public static final String SEARCH_TYPE_FINANCIAL = "Financial";

    // Errors Codes
    public static final int ERROR_CODE_VERSION_NO_INTERNET = 0;
    public static final int ERROR_CODE_VERSION_NETWORK_CONNECTION = 1;
    public static final int ERROR_CODE_VERSION_UNKNOWN = 2;
    public static final int ERROR_CODE_VERSION_NOT_EXIST = 3;

    public static final int ERROR_CODE_MOBILE_NUMBER = 4;
    public static final int ERROR_CODE_SERVER = 5;
    public static final int ERROR_CODE_VERIFICATION_CODE = 6;

    public static final int ERROR_CODE_USER_NO_INTERNET = 7;
    public static final int ERROR_CODE_USER_NETWORK_CONNECTION = 8;
    public static final int ERROR_CODE_USER_UNKNOWN = 9;

    public static final int ERROR_CODE_NO_INTERNET = 10;
    public static final int ERROR_CODE_NETWORK_CONNECTION = 11;
    public static final int ERROR_CODE_UNKNOWN = 12;


    // Date
    private static final String DATE_FORMAT_SHOW = "EEE dd MMM yyyy";

    public static String showDate(Date date, String language) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHOW, new Locale(language));
        return dateFormat.format(date);
    }

    // Comparators to sort Packages and Cashes by Date
    public static final Comparator<Package> PACKAGES_COMPARATOR = new Comparator<Package>() {
        @Override
        public int compare(Package type1, Package type2) {
            return type2.getDate().compareTo(type1.getDate());
        }
    };

    public static final Comparator<Cash> CASH_COMPARATOR = new Comparator<Cash>() {
        @Override
        public int compare(Cash type1, Cash type2) {
            return type2.getDate().compareTo(type1.getDate());
        }
    };

    // Item Decoration for Recycler View
    public static RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 24;
                outRect.right = 24;
                outRect.top = 12;
                outRect.bottom = 12;
            }
        };
    }


    // suffixes
    public static CharSequence suffixText(String text, String suffix) {
        SpannableString spannableString = new SpannableString(suffix);
        spannableString.setSpan(new RelativeSizeSpan(0.7f), 0, suffix.length(), 0);
        return TextUtils.concat(text, "  ", spannableString);
    }


    public static Boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

            } else {
                return false;
            }

        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }

    }

}
