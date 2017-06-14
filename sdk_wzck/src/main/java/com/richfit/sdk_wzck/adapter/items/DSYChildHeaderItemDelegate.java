package com.richfit.sdk_wzck.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.R;

/**
 * Created by monday on 2016/11/20.
 */

public class DSYChildHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.wzck_item_ds_detail_child_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return Global.CHILD_NODE_HEADER_TYPE == item.getViewType();
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {

    }
}
