package com.richfit.module_xngd.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;

/**
 * Created by monday on 2017/8/16.
 */

public class XNGDParentHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.xngd_item_ds_parent_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return Global.PARENT_NODE_HEADER_TYPE == item.getViewType();
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.refLineNum, item.lineNum);
        holder.setText(R.id.materialNum, item.materialNum);
        holder.setText(R.id.materialDesc, item.materialDesc);
        holder.setText(R.id.materialGroup, item.materialGroup);
        holder.setText(R.id.materialUnit, item.unit);
        //应发数量
        holder.setText(R.id.actQuantity, item.actQuantity);
        //累计数量
        holder.setText(R.id.totalQuantity, item.totalQuantity);
        //工厂
        holder.setText(R.id.work, item.workCode);
        //库存地点
        holder.setText(R.id.inv, item.invCode);
        holder.setText(R.id.moveCause, item.moveCause);
    }
}
