package com.richfit.sdk_sxcl.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_sxcl.adapter.items.LocQTChildHeaderItemDelegate;
import com.richfit.sdk_sxcl.adapter.items.LocQTChildItemDelegate;
import com.richfit.sdk_sxcl.adapter.items.LocQTParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/5/25.
 */

public class LocQTDetailAdapter  extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public LocQTDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new LocQTParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new LocQTChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new LocQTChildItemDelegate());
    }
}
