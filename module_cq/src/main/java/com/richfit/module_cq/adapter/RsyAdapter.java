package com.richfit.module_cq.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.adapter.delegate.RsyChildHeadItemDelegate;
import com.richfit.module_cq.adapter.delegate.RsyChildItemDelegate;
import com.richfit.module_cq.adapter.delegate.RsyParentHeadItemDelegate;
import java.util.List;

/**
 * Created by monday on 2018/1/31.
 */

public class RsyAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public RsyAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new RsyParentHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new RsyChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new RsyChildItemDelegate());
    }
}