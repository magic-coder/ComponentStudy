package com.richfit.module_xngd.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.adapter.items.XNGDAOChildHeaderItemDelegate;
import com.richfit.module_xngd.adapter.items.XNGDAOChildItemDelegate;
import com.richfit.module_xngd.adapter.items.XNGDAOParentHeaderItemDelegate;

import java.util.List;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAODetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {

    public XNGDAODetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
        addItemViewDelegate(Global.PARENT_NODE_HEADER_TYPE, new XNGDAOParentHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_HEADER_TYPE, new XNGDAOChildHeaderItemDelegate());
        addItemViewDelegate(Global.CHILD_NODE_ITEM_TYPE, new XNGDAOChildItemDelegate());
    }

    /**
     * 内向交货的验收过账前必须检查全检数量和抽检数量必须小于累计实收数量
     *
     * @return
     */
    public boolean isTransferValid() {
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            RefDetailEntity item = getItem(i);
            float totalQ = CommonUtil.convertToFloat(item.totalQuantity, 0.0F);
            if (item.getViewType() != Global.PARENT_NODE_HEADER_TYPE ||
                    TextUtils.isEmpty(item.totalQuantity) ||
                    Float.compare(totalQ, 0.0F) <= 0)
                continue;
            float allQ = CommonUtil.convertToFloat(item.allQuantity, 0.0F);
            float partQ = CommonUtil.convertToFloat(item.partQuantity, 0.0F);
            if (Float.compare(allQ, totalQ) > 0 || Float.compare(partQ, totalQ) > 0) {
                return false;
            }
        }
        return true;
    }

}
