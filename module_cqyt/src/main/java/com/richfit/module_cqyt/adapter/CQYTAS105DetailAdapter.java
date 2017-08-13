package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.adapter.items.CQYTAS105ParentHeaderItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildHeaderItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/3/7.
 */

public class CQYTAS105DetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public CQYTAS105DetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new CQYTAS105ParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new ASYChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new ASYChildItemDelegate());
    }

}
