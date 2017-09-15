package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315ChildItemDelegate extends ASYChildItemDelegate {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_msy315_child_item;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.cqyt_tv_quantity_custom, item.quantityCustom)
                .setText(R.id.cqyt_tv_location_type, item.locationType);
    }
}