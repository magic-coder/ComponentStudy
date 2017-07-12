package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_ubsto_detail_child_item;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
       holder.setText(R.id.location,item.location)
               .setText(R.id.batchFlag,item.batchFlag)
               .setText(R.id.quantity,item.quantity)
               .setText(R.id.cqyt_tv_quantity_custom,item.quantityCustom);
    }
}
