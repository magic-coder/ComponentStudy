package com.richfit.sdk_wzyk.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzyk.R;

/**
 * Created by monday on 2017/2/10.
 */

public class MSYChildHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.wzyk_item_msy_detail_child_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return Global.CHILD_NODE_HEADER_TYPE == item.getViewType();
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {

    }
}
