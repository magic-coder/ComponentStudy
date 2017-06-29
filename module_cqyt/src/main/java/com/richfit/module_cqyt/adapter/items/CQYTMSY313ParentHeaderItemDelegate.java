package com.richfit.module_cqyt.adapter.items;


import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.adapter.items.MSYParentHeaderItemDelegate;

/**
 * Created by monday on 2017/3/19.
 */

public class CQYTMSY313ParentHeaderItemDelegate extends MSYParentHeaderItemDelegate {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_msy313_detail_parent_header;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.cqyt_total_quantity_custom,item.totalQuantityCustom);
    }
}
