package com.richfit.sdk_sxcl.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_sxcl.R;

/**
 * Created by monday on 2017/5/25.
 */

public class LocQTChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.sxcl_item_locqt_detail_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        if ("S".equalsIgnoreCase(item.shkzg)) {
            holder.setText(R.id.xLocation, "");
            holder.setText(R.id.sLocation, item.location);
        } else if ("H".equalsIgnoreCase(item.shkzg)) {
            holder.setText(R.id.sLocation, "");
            holder.setText(R.id.xLocation, item.location);
        }
        holder.setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag)
                .setText(R.id.quantity, item.quantity)
                .setText(R.id.tv_location_type,item.locationType);
    }
}