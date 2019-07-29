package io.taucoin.android.wallet.module.view.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.taucoin.android.wallet.module.view.manage.DefinePasswordFragment;

public class IntroAdapter extends FragmentPagerAdapter {


    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        switch (position)
        {
            case 0:
                return new IntroPageOneFragment();
            case 1:
                return new IntroTowPageFragment();
            case 2:
                return new DefinePasswordFragment();

        }



        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
