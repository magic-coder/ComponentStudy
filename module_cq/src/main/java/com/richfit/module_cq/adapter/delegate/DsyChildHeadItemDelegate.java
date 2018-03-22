package com.richfit.module_cq.adapter.delegate;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.R;

/**
 * Created by monday on 2018/1/31.
 */

public class DsyChildHeadItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqzt_item_dsy_child_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return Global.CHILD_NODE_HEADER_TYPE == item.getViewType();
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {

    }
}
