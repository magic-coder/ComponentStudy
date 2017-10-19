package com.richfit.module_xngd.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.adapter.items.ASYChildItemDelegate;

/**
 * Created by monday on 2017/10/18.
 */

public class XNGDRSChildItemDelegate extends ASYChildItemDelegate {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.xngd_item_rs_detail_child_item;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.xngd_tv_money, item.money);
    }
}
