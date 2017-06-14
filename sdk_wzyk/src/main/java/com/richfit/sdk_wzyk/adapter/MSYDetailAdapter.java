package com.richfit.sdk_wzyk.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzyk.adapter.items.MSYChildHeaderItemDelegate;
import com.richfit.sdk_wzyk.adapter.items.MSYChildItemDelegate;
import com.richfit.sdk_wzyk.adapter.items.MSYParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/3/19.
 */

public class MSYDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public MSYDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new MSYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new MSYChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new MSYChildItemDelegate());
    }

}
