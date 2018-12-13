package com.tau.view.expanableRV;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;
import com.mofei.tau.R;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description:
 */
class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder{

    GroupVH(View itemView) {
        super(itemView);
    } // this method is used for partial update.Which means when expand status changed,only a part of this view need to invalidate

    @Override
    protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
        // 1.只更新左侧展开、闭合箭头
        //foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding); // 2.默认刷新整个Item
        relatedAdapter.notifyItemChanged(getAdapterPosition());

    }


    class ChildVH extends RecyclerView.ViewHolder {

        ChildVH(View itemView) {
            super(itemView);
        }
    }
}


