package com.mofei.tau.view.expanableRV;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

/**
 * Created by ly on 18-11-20
 *
 * @version 1.0
 * @description:
 */
public class GroupBean implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<GroupBean.ChildBean>{

    List<ChildBean> mList;
    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public ChildBean getChildAt(int childIndex) {
        return mList.size() <= childIndex ? null : mList.get(childIndex);
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }


    public class ChildBean {

    }
}





