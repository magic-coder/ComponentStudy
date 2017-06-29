package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTMSY313ChildHeaderItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTMSY313ChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTMSY313ParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTMSY313DetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public CQYTMSY313DetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new CQYTMSY313ParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new CQYTMSY313ChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new CQYTMSY313ChildItemDelegate());
    }
}
