package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTAS103ChildHeadItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_as103_detail_child_head;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return Global.CHILD_NODE_HEADER_TYPE == item.getViewType();
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity refDetailEntity, int position) {
        holder.setText(R.id.quantity,"到货数量");
    }
}