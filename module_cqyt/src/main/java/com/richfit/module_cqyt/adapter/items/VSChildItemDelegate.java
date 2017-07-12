package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/7/3.
 */

public class VSChildItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_vs_child_head;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.CHILD_NODE_ITEM_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.refLineNum, item.lineNum)
                .setText(R.id.orderQuantity, item.orderQuantity)
                .setText(R.id.cqyt_tv_ref_num, item.recordNum);
    }
}
