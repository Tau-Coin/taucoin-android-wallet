package io.taucoin.android.wallet.module.view.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mofei.tau.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroTowPageFragment extends Fragment {


    public IntroTowPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_tow_page, container, false);
    }

}