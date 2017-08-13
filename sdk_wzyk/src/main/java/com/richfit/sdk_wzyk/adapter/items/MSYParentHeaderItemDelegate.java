package com.richfit.sdk_wzyk.adapter.items;


import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzyk.R;

/**
 * Created by monday on 2017/3/19.
 */

public class MSYParentHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.wzyk_item_msy_detail_parent_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.refLineNum,item.lineNum);
        holder.setText(R.id.materialNum,item.materialNum);
        holder.setText(R.id.materialDesc,item.materialDesc);
        holder.setText(R.id.materialGroup,item.materialGroup);
        holder.setText(R.id.materialUnit,item.unit);
        //应发数量
        holder.setText(R.id.actQuantity,item.actQuantity);
        //累计数量
        holder.setText(R.id.totalQuantity,item.totalQuantity);
        //发出工厂
        holder.setText(R.id.sendWork,item.workCode);
        //接收工厂
        holder.setText(R.id.recWork,item.recWorkCode);
        //发出库位
        holder.setText(R.id.sendInv,item.invCode);
        //接收库位
        holder.setText(R.id.recInv,item.recInvCode);
    }
}
