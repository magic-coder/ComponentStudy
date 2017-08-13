package com.richfit.module_xngd.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAOChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.xngd_item_detail_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.location, item.location)
                 .setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag)
                .setText(R.id.quantity, item.quantity);
    }
}