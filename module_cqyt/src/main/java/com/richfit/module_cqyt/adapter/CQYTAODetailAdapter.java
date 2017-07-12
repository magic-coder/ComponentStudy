package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTAOChildHeadItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTAOChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTAOParentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAODetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public CQYTAODetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new CQYTAOParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new CQYTAOChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new CQYTAOChildItemDelegate());
    }
}