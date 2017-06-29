package com.richfit.sdk_wzck.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.adapter.items.DSYChildHeaderItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYChildItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYParentHeaderItemDelegate;

import java.util.List;

/**
 * 有参考物资(有参考)标志出库
 * Created by monday on 2016/11/16.
 */

public class DSYDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public DSYDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new DSYParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new DSYChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new DSYChildItemDelegate());
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
