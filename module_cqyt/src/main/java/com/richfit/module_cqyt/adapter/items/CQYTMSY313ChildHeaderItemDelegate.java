package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.adapter.items.MSYChildHeaderItemDelegate;

/**
 * Created by monday on 2017/2/10.
 */

public class CQYTMSY313ChildHeaderItemDelegate extends MSYChildHeaderItemDelegate {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_msy313_detail_child_head;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {

    }
}
