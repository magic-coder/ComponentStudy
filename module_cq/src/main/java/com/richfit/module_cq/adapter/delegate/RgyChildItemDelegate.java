package com.richfit.module_cq.adapter.delegate;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.R;

/**
 * Created by monday on 2018/1/31.
 */

public class RgyChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqzt_item_dsy_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.location, Global.DEFAULT_LOCATION.equalsIgnoreCase(item.location) ? "" : item.location);
        holder.setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag);
        holder.setText(R.id.quantity, item.quantity);
    }
}
