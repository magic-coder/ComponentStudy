package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.InvyChildHeadItemDelegate;
import com.richfit.module_cqyt.adapter.items.InvyChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.InvyParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2018/3/19.
 */

public class InvyAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public InvyAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new InvyParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new InvyChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new InvyChildItemDelegate());
    }
}
