package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.VSChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.VSChildeHeadItemDelegate;
import com.richfit.module_cqyt.adapter.items.VSParentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTVSADetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public CQYTVSADetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new VSParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new VSChildeHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new VSChildItemDelegate());
    }
}