package io.taucoin.android.wallet.module.view.manage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mofei.tau.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.taucoin.android.wallet.module.bean.HelpBean;
import io.taucoin.foundation.util.DrawablesUtil;

public class HelpAdapter extends BaseAdapter {

    private List<HelpBean> list = new ArrayList<>();


    void setListData(List<HelpBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HelpBean helpBean = list.get(position);
        viewHolder.tvHelpTitle.setText(helpBean.getTitle());
        DrawablesUtil.setUnderLine(viewHolder.tvHelpTitle);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_help_title)
        TextView tvHelpTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
