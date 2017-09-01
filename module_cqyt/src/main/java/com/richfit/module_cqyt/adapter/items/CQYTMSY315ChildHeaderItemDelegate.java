package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.adapter.items.ASYChildHeaderItemDelegate;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315ChildHeaderItemDelegate extends ASYChildHeaderItemDelegate{
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_msy315_child_head;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {

    }
}
