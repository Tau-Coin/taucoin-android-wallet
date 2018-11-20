package com.mofei.tau.view.expanableRV;

import android.view.ViewGroup;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description: // !!注意这里继承时候使用的泛型，分别为上面提到的Bean和ViewHolder
 */
public class Adapter extends BaseExpandableRecyclerViewAdapter<GroupBean, GroupBean.ChildBean, GroupVH, GroupVH.ChildVH> {

    @Override
    public int getGroupCount() {
        // 父节点个数
        return 0;
    }

    @Override
    public GroupBean getGroupItem(int groupIndex) {
        // 获取父节点
        return null;
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return null;
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, GroupBean groupBean, boolean isExpand) {

    }

    @Override
    public GroupVH.ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return null;
    }

    @Override
    public void onBindChildViewHolder(GroupVH.ChildVH holder, GroupBean groupBean, GroupBean.ChildBean childBean) {

    }
}


