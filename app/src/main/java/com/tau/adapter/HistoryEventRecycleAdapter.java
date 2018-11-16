package com.mofei.tau.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.transaction.TransactionHistory;

import java.util.List;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */
public class HistoryEventRecycleAdapter extends RecyclerView.Adapter<HistoryEventRecycleAdapter.HistoryViewHolder>  {

    Context context;
    List<TransactionHistory> list;


    public HistoryEventRecycleAdapter(Context context,List<TransactionHistory> list){
        this.context=context;
        this.list=list;

    }


    //Initialization control
    public  class HistoryViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        public LinearLayout deletell;
        TextView resultTextView;
        TextView addressTextView;
        TextView amountTextView;
        TextView dateTextView;
        TextView txidTextView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.ll_item);
            resultTextView=itemView.findViewById(R.id.result);
            txidTextView=itemView.findViewById(R.id.txid);
            addressTextView=itemView.findViewById(R.id.to_address);
            amountTextView=itemView.findViewById(R.id.amount_);
            dateTextView=itemView.findViewById(R.id.date);

            deletell=itemView.findViewById(R.id.ll_hidden);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history,parent,false);

        HistoryViewHolder historyViewHolder=new HistoryViewHolder(view);

        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

        TransactionHistory transactionHistory=list.get(position);

        holder.resultTextView.setText(transactionHistory.getResult()+"");
        holder.txidTextView.setText(transactionHistory.getTxId());
        holder.addressTextView.setText(transactionHistory.getToAddress());
        holder.amountTextView.setText(transactionHistory.getValue());
        holder.dateTextView.setText(transactionHistory.getTime()+"");

    }

    @Override
    public int getItemCount() {

        return list.size();
    }




}
