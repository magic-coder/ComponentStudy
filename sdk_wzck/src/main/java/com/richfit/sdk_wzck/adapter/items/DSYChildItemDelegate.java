package com.richfit.sdk_wzck.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.R;

/**
 * Created by monday on 2016/11/20.
 */

public class DSYChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.wzck_item_ds_detail_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.location, item.location);
        holder.setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag);
        holder.setText(R.id.quantity, item.quantity);
        holder.setText(R.id.specialInvFlag, item.specialInvFlag);
        holder.setText(R.id.specialInvNum, item.specialInvNum);
        holder.setText(R.id.tv_location_type,item.locationType);
    }
}
