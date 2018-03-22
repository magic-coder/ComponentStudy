package com.richfit.module_cq.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.adapter.delegate.As101ChildHeadItemDelegate;
import com.richfit.module_cq.adapter.delegate.As101ChildItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildHeaderItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;
import com.richfit.sdk_wzrk.adapter.items.ASYParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2018/1/31.
 */

public class As101Adapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public As101Adapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE,new ASYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE,new As101ChildHeadItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE,new As101ChildItemDelegate());
    }

    /**
     * 销售出库的时候需要判断所有行明细都做完才能过账
     *
     * @return
     */
    public boolean isTransferValid() {
        if (mVisibleNodes != null) {
            for (RefDetailEntity item : mVisibleNodes) {
                //如果累计数量为空或者等于0，那么认为该明细没有采集过数据(注意必须仅检查父节点的totalQuantity)
                if (item.isRoot() && (TextUtils.isEmpty(item.totalQuantity) || "0".equals(item.totalQuantity))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
