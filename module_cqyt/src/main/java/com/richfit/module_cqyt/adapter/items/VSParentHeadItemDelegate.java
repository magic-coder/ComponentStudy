package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/7/3.
 */

public class VSParentHeadItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_vs_parent_head;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialGroup, item.materialGroup)
                .setText(R.id.materialDesc, item.materialDesc);
        holder.setText(R.id.cqyt_s_total_quantity, item.quantityS);
        holder.setText(R.id.cqyt_h_total_quantity, item.quantityH);
    }
}
