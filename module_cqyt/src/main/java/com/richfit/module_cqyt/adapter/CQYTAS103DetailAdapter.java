package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTAS103ChildHeadItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTAS103ChildItemDelegate;
import com.richfit.module_cqyt.adapter.items.CQYTAS103ParentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTAS103DetailAdapter  extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public CQYTAS103DetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new CQYTAS103ParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new CQYTAS103ChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new CQYTAS103ChildItemDelegate());
    }
}