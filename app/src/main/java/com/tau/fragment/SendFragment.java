package com.mofei.tau.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.activity.HistoryTransationActivity;

/**
 *
 * create an instance of this fragment.
 */
public class SendFragment extends Fragment{
    private EditText amountET,addressET;
    //private Button sendBt;
    private RelativeLayout historyRl;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_send, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        amountET=view.findViewById(R.id.amount);
        addressET=view.findViewById(R.id.address);
       // sendBt=view.findViewById(R.id.send);

       /* sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount=amountET.getText().toString().trim();
                String address=addressET.getText().toString().trim();
            }
        });*/

        historyRl=view.findViewById(R.id.history);
        historyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HistoryTransationActivity.class));
            }
        });

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
