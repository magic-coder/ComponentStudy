package com.richfit.module_cq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.adapter.items.DSYChildHeaderItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYChildItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2018/1/31.
 */

public class RgyAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public RgyAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new DSYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new DSYChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new DSYChildItemDelegate());
    }
}
