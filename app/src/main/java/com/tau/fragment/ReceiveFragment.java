package com.mofei.tau.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mofei.tau.R;
import com.mofei.tau.util.ZXingUtils;

/**
 *
 * create an instance of this fragment.
 */
public class ReceiveFragment extends Fragment {

    private ImageView paymentImageViewInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paymentImageViewInfo=view.findViewById(R.id.payment_info);

        //二维码生成
        Bitmap addressIV= ZXingUtils.createQRImage("qwweewfrqcevgrwgtwgt",300,300);
        paymentImageViewInfo.setImageBitmap(addressIV);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
