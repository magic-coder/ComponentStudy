package com.richfit.module_xngd.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.adapter.items.XNGDParentHeaderItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYChildHeaderItemDelegate;
import com.richfit.sdk_wzck.adapter.items.DSYChildItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/8/16.
 */

public class XNGDDSDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public XNGDDSDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new XNGDParentHeaderItemDelegate());
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
