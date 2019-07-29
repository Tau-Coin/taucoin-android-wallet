package io.taucoin.android.wallet.module.view.manage;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.mofei.tau.R;

import io.taucoin.android.wallet.module.view.main.MainActivity;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefinePasswordFragment extends Fragment {


    private boolean isFirst = true;
    private PinLockView mPinLockView;
    private TextView tv_title, tv_thanks;
    private String pass_one = "";
    private LinearLayout li_root;
    private ImageView logo;

    public DefinePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_intro_tree_page, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        tv_title = view.findViewById(R.id.tv_title);
        tv_thanks = view.findViewById(R.id.tv_thanks);
        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        li_root = view.findViewById(R.id.li_root);
        logo = view.findViewById(R.id.logo);
        IndicatorDots mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (isFirst) {
                    tv_title.setText("Repeat the Password");
                    mPinLockView.resetPinLockView();
                    pass_one = pin;
                    isFirst = false;
                } else {
                    if (pass_one.equals(pin)) {
                        li_root.setVisibility(View.GONE);
//                        tv_thanks.setVisibility(View.VISIBLE);

                        SharedPreferencesHelper.getInstance().putString("code", pin);

                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                        fadeIn.setRepeatCount(0);
                        fadeIn.setDuration(1500);

                        fadeIn.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        AnimationSet animation = new AnimationSet(false); //change to false
                        animation.addAnimation(fadeIn);

                        logo.setVisibility(View.VISIBLE);
                        logo.setAnimation(animation);






                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Passwords Are Not...");
                        builder.setTitle("Try Again");
                        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        Toast.makeText(getActivity(), "passwords are not equal", Toast.LENGTH_LONG).show();
                        tv_title.setText("Enter New Local Password");
                        mPinLockView.resetPinLockView();
                        isFirst = true;
                    }
                }

            }

            @Override
            public void onEmpty() {
                Log.i("CODE", "empty");

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                Log.i("CODE", "change");

            }
        });

    }

}
