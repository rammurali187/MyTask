package com.ram.my.movies.utils;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ram.my.movies.R;
import com.ram.my.movies.ui.adapter.ViewPagerAdapter;

import timber.log.Timber;

public class ViewPager {

    private Context context;
    android.support.v4.view.ViewPager mpagerContainer;
    LinearLayout mSliderDots;

    public ViewPager(Context context, android.support.v4.view.ViewPager mpagerContainer,LinearLayout mSliderDots) {
        this.context = context;
        this.mpagerContainer = mpagerContainer;
        this.mSliderDots = mSliderDots;
    }

    public void viewpagerinit(String[] positionone,String[] positontwo) {

        try {

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context,positionone,positontwo);
            mpagerContainer.setAdapter(viewPagerAdapter);
            mpagerContainer.setClipToPadding(false);
            int dotscount = viewPagerAdapter.getCount();
            ImageView[] dots = new ImageView[dotscount];

            for(int i = 0; i < dotscount; i++){
                dots[i] = new ImageView(context);
                dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                mSliderDots.addView(dots[i], params);

            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));

            mpagerContainer.addOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mpagerContainer.setAdapter(viewPagerAdapter);

        }catch (Exception ex){
            Timber.d("View Pager ex: " + ex.getMessage().toString());
        }


    }
}
