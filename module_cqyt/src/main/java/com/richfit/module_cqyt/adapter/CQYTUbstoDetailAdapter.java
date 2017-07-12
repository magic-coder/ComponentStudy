package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTUbstoChildHeadItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTUbstoChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTUbstoParentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public CQYTUbstoDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new CQYTUbstoParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new CQYTUbstoChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new CQYTUbstoChildItemDelegate());
    }
}