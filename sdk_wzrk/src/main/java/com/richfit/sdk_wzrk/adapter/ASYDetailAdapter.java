package com.richfit.sdk_wzrk.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzrk.adapter.items.ASYChildHeaderItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYParentHeaderItemDelegate;

import java.util.List;

/**
 * 标准物资入库有参考明细界面适配器
 * Created by monday on 2017/3/17.
 */

public class ASYDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public ASYDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new ASYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new ASYChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new ASYChildItemDelegate());
    }



}
