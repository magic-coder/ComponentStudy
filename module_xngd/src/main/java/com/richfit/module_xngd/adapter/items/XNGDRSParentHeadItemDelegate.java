package com.richfit.module_xngd.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.adapter.items.ASYParentHeaderItemDelegate;

/**
 * Created by monday on 2017/10/18.
 */

public class XNGDRSParentHeadItemDelegate extends ASYParentHeaderItemDelegate {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.xngd_item_rs_detail_parent_head;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity data, int position) {
        super.convert(holder,data,position);
        //累计金额
        holder.setText(R.id.xngd_tv_total_money,data.totalMoney);
    }

}
