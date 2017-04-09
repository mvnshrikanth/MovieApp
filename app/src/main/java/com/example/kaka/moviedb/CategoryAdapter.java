package com.example.kaka.moviedb;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Kaka on 4/6/2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MovieOverviewsFragment();
        } else if (position == 1) {
            return new MovieTrailersFragment();
        } else {
            return new MovieReviewsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.str_category_overview);
        } else if (position == 1) {
            return mContext.getString(R.string.str_category_trailers);
        } else {
            return mContext.getString(R.string.str_category_reviews);
        }
    }

}
