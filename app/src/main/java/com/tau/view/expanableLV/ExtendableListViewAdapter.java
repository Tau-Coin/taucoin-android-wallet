package com.mofei.tau.view.expanableLV;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofei.tau.R;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.transaction.TXChild;
import com.mofei.tau.transaction.TXGroup;
import com.mofei.tau.transaction.TransactionHistory;
import com.mofei.tau.transaction.UTXORecord;
import com.mofei.tau.util.L;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description:
 */
public class ExtendableListViewAdapter extends BaseExpandableListAdapter{

    Context context;

    private List<TXGroup> groupArray;
    private List<List<TXChild>> childArray;

    public ExtendableListViewAdapter(Context context,List<TXGroup> groupArray,List<List<TXChild>> childArray){
        this.context=context;
        this.groupArray=groupArray;
        this.childArray=childArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray == null ? 0 : groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition) == null ? 0 : childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    String time;
    String statusString;
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
    // 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_partent_item,parent,false);
            groupViewHolder = new GroupViewHolder();
           // groupViewHolder.txImageview=convertView.findViewById(R.id.tx_img);
            groupViewHolder.txNum=convertView.findViewById(R.id.tx_num);
            groupViewHolder.txTime=convertView.findViewById(R.id.tx_time);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        //groupViewHolder.txImageview.setText(groupString[groupPosition]);
        groupViewHolder.txNum.setText("- "+groupArray.get(groupPosition).getAmount());
        time=groupArray.get(groupPosition).getTime();

       int confoirmation=groupArray.get(groupPosition).getConfoirmation();
        if (confoirmation<2){
            groupViewHolder.txNum.setTextColor(Color.RED);
            groupViewHolder.txTime.setTextColor(Color.RED);
           // groupViewHolder.txTime.setText(groupArray.get(groupPosition).getTime());
            groupViewHolder.txTime.setText("");
            L.e("groupArray");
        } else  {
            groupViewHolder.txNum.setTextColor(Color.BLACK);
            groupViewHolder.txTime.setTextColor(Color.BLACK);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(time) * 1000));
            groupViewHolder.txTime.setText(date);
        }
       /* L.e("time="+time);
        if (time.equals("0")){
            groupViewHolder.txNum.setTextColor(Color.RED);
            groupViewHolder.txTime.setTextColor(Color.RED);
            groupViewHolder.txTime.setText(groupArray.get(groupPosition).getTime());
            L.e("groupArray");
        }else {
            groupViewHolder.txNum.setTextColor(Color.BLACK);
            groupViewHolder.txTime.setTextColor(Color.BLACK);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(time) * 1000));
            groupViewHolder.txTime.setText(date);
        }*/
        return convertView;
    }

    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_child_item,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.txSatus=convertView.findViewById(R.id.tx_status);
            childViewHolder.txAddress = (TextView)convertView.findViewById(R.id.tx_address);
            childViewHolder.txId=convertView.findViewById(R.id.tx_id);
           // childViewHolder.txFee=convertView.findViewById(R.id.tx_fee);
           // childViewHolder.txBlockHeight=convertView.findViewById(R.id.tx_block_height);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (time.equals("0")){
            childViewHolder.txSatus.setTextColor(Color.RED);
        }else {
            int color = Color.parseColor("#2196F3");
            childViewHolder.txSatus.setTextColor(color);
        }
        statusString=childArray.get(groupPosition).get(childPosition).getStatus();
        childViewHolder.txSatus.setText(statusString);
        String to_address=SharedPreferencesHelper.getInstance(context).getString("to_address","");
        childViewHolder.txAddress.setText(to_address);
        childViewHolder.txId.setText(childArray.get(groupPosition).get(childPosition).getTxId());
        //childViewHolder.txFee.setText(childArray.get(groupPosition).get(childPosition).getTxFee());
        // childViewHolder.txBlockHeight.setText(childArray.get(groupPosition).get(childPosition).getTxBlockHeight());
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        //ImageView txImageview;
        TextView txNum;
        TextView txTime;
    }

    static class ChildViewHolder {
        TextView txSatus;
        TextView txAddress;
        TextView txId;
        TextView txFee;
       // TextView txBlockHeight;

    }
}
