package com.richfit.module_mcq.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;

/**
 * 标准物资入库子节点的明细Item代理
 * Created by monday on 2017/3/17.
 */

public class ASChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.mcq_item_as_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.location, Global.DEFAULT_LOCATION.equalsIgnoreCase(item.location) ? "" : item.location);
        holder.setText(R.id.quantity, item.quantity);
        holder.setText(R.id.tv_location_type, item.locationType);
        holder.setText(R.id.mcq_quantity_custom, item.quantityCustom);
    }
}
