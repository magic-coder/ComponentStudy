package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTMSY315ChildHeaderItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTMSY315ChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTMSY315ParentHeaderItemDelagate;

import java.util.List;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315DetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public CQYTMSY315DetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new CQYTMSY315ParentHeaderItemDelagate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new CQYTMSY315ChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new CQYTMSY315ChildItemDelegate());
    }
}