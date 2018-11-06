package com.mofei.tau.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.activity.HistoryTransationActivity;
import com.mofei.tau.src.com.google.bitcoin.core.NetworkParameters;
import com.mofei.tau.src.io.taucoin.android.wallet.Wallet;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.CreateTransactionResult;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.Transaction;
import com.mofei.tau.src.io.taucoin.android.wallet.transactions.TransactionFailReason;
import com.mofei.tau.util.L;

import java.math.BigInteger;
import java.util.HashMap;

/**
 *
 * create an instance of this fragment.
 */
public class SendFragment extends Fragment{
    private EditText amountET,addressET;
    private CardView sendCV;
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

        intiEvent(view);

    }

    private void intiEvent(View view) {
        amountET=view.findViewById(R.id.amount);
        addressET=view.findViewById(R.id.address);
        sendCV=view.findViewById(R.id.send_tau);
        sendCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount=amountET.getText().toString().trim();
                String address=addressET.getText().toString().trim();

                HashMap<String, BigInteger> receipts = new HashMap<String, BigInteger>();
                receipts.put("TNg7dixGbuNzNvdfqPLCYCp61A7iGd3EWs", new BigInteger("700000000", 10));

             //   BigInteger amountBigInt=new BigInteger(String.valueOf(Integer.parseInt(amount)*10000000),10);
                //receipts.put(address,amountBigInt);

                Wallet wallet = Wallet.getInstance();
                Transaction tx = new Transaction(NetworkParameters.mainNet());
                CreateTransactionResult result = wallet.createTransaction(receipts, false, null, tx);
                if (result.failReason == TransactionFailReason.NO_ERROR) {
                    L.e("Create tx success");
                    L.e(tx.toString());
                    L.e("Hex:");
                    L.e(tx.dumpIntoHexStr());
                } else {
                    L.e("Create tx failed");
                    L.e("error code:" + result.failReason.getCode() + ", " + result.failReason.getMsg());
                }
            }
        });

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
