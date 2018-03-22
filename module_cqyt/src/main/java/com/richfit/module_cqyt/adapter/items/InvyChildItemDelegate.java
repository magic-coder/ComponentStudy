package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2018/3/19.
 */

public class InvyChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_invy_child_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {

        holder.setText(com.richfit.sdk_xxcx.R.id.inv, item.invCode)
                .setText(com.richfit.sdk_xxcx.R.id.location, item.location)
                .setText(com.richfit.sdk_xxcx.R.id.batchFlag, item.batchFlag)
                .setText(com.richfit.sdk_xxcx.R.id.invQuantity, item.invQuantity);

    }
}