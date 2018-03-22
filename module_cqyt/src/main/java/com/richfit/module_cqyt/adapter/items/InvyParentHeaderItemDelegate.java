package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2018/3/19.
 */

public class InvyParentHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_invy_parent_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(com.richfit.sdk_xxcx.R.id.lineNum,item.lineNum)
                .setText(com.richfit.sdk_xxcx.R.id.materialNum,item.materialNum)
                .setText(com.richfit.sdk_xxcx.R.id.materialDesc,item.materialDesc)
                .setText(com.richfit.sdk_xxcx.R.id.materialGroup,item.materialGroup)
                .setText(com.richfit.sdk_xxcx.R.id.materialUnit,item.unit)
                .setText(com.richfit.sdk_xxcx.R.id.work,item.workCode)
                .setText(com.richfit.sdk_xxcx.R.id.inv,item.invCode)
                .setText(com.richfit.sdk_xxcx.R.id.orderQuantity,item.orderQuantity);
    }
}
