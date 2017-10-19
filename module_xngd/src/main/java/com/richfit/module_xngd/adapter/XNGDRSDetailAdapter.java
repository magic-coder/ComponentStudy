package com.richfit.module_xngd.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.adapter.items.XNGDRSChildHeadItemDelegate;
import com.richfit.module_xngd.adapter.items.XNGDRSChildItemDelegate;
import com.richfit.module_xngd.adapter.items.XNGDRSParentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/10/18.
 */

public class XNGDRSDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public XNGDRSDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new XNGDRSParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new XNGDRSChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new XNGDRSChildItemDelegate());
    }
}
