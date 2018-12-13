package com.tau.adapter;

import java.util.List;

/**
 * Created by ly on 18-11-19
 *
 * @version 1.0
 * @description: The relationship between group items and sub items is 1 to many.
 */
public class DataListTree<K,V> {
    private K mGroupItem;
    private List<V> mSubItem;
    public DataListTree(K groupItem, List<V> subItem) {
        mGroupItem = groupItem;
        mSubItem = subItem;
    } public K getGroupItem() {
        return mGroupItem;
    } public List<V> getSubItem() {
        return mSubItem;
    }
}
