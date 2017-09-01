package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.adapter.items.ASChildHeadItemDelegate;
import com.richfit.module_mcq.adapter.items.ASChildItemDelegate;
import com.richfit.module_mcq.adapter.items.ASParentHeadItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;

import java.util.List;

/**
 * 标准物资入库有参考明细界面适配器
 * Created by monday on 2017/3/17.
 */

public class MCQASDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public MCQASDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new ASParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new ASChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new ASChildItemDelegate());
    }

}
