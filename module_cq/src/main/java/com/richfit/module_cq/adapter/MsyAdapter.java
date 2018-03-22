package com.richfit.module_cq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.adapter.delegate.As101ChildHeadItemDelegate;
import com.richfit.module_cq.adapter.delegate.As101ChildItemDelegate;
import com.richfit.module_cq.adapter.delegate.MsyChildHeadItemDelegate;
import com.richfit.module_cq.adapter.delegate.MsyChildItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYParentHeaderItemDelegate;
import com.richfit.sdk_wzyk.adapter.items.MSYChildHeaderItemDelegate;
import com.richfit.sdk_wzyk.adapter.items.MSYChildItemDelegate;
import com.richfit.sdk_wzyk.adapter.items.MSYParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2018/1/31.
 */

public class MsyAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public MsyAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new MSYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new MsyChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new MsyChildItemDelegate());
    }

}
