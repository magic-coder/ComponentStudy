package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.adapter.items.ASChildHeadItemDelegate;
import com.richfit.module_mcq.adapter.items.ASChildItemDelegate;
import com.richfit.module_mcq.adapter.items.ASParentHeadItemDelegate;
import com.richfit.module_mcq.adapter.items.DSChildHeadItemDelegate;
import com.richfit.module_mcq.adapter.items.DSChildItemDelegate;
import com.richfit.module_mcq.adapter.items.DSPrarentHeadItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public MCQDSDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new DSPrarentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new DSChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new DSChildItemDelegate());
    }

}