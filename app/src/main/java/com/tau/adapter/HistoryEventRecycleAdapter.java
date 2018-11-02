package com.mofei.tau.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mofei.tau.R;

import java.util.List;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */
public class HistoryEventRecycleAdapter extends RecyclerView.Adapter<HistoryEventRecycleAdapter.HistoryViewHolder>  {

    Context context;
    List<String> list;


    public HistoryEventRecycleAdapter(Context context,List<String> list){
        this.context=context;
        this.list=list;

    }


    //Initialization control
    public  class HistoryViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        TextView textView;

        public HistoryViewHolder(View itemView) {

            super(itemView);

            linearLayout=itemView.findViewById(R.id.ll_item);
            textView=itemView.findViewById(R.id.text_);
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

        String s=list.get(position);

        holder.textView.setText(s);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }




}
