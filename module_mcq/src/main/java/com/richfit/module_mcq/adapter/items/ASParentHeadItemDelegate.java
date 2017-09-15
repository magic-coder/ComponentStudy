package com.richfit.module_mcq.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;

/**
 * 标准物资出库父节点抬头的Item代理
 * Created by monday on 2017/3/17.
 */

public class ASParentHeadItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.mcq_item_as_parent_head;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity data, int position) {
        holder.setText(R.id.lineNum, data.lineNum)
                //物料号
                .setText(R.id.materialNum, data.materialNum)
                //物资描述
                .setText(R.id.materialDesc, data.materialDesc)
                //物料组
                .setText(R.id.materialGroup, data.materialGroup)
                //计量单位
                .setText(R.id.materialUnit, data.unit)
                //应收数量
                .setText(R.id.actQuantity, data.actQuantity)
                //累计实收数量
                .setText(R.id.totalQuantity, data.totalQuantity)
                //工厂
                .setText(R.id.work, data.workCode)
                //库存地点
                .setText(R.id.inv, data.invCode)
                .setText(R.id.mcq_material_unit_custom, data.unitCustom)
                .setText(R.id.mcq_act_quantity_custom, data.actQuantityCustom)
                .setText(R.id.mcq_total_quantity_custom, data.totalQuantityCustom)
                .setText(R.id.mcq_qmFlag, data.qmFlag)
                //单据状态
                .setText(R.id.mcq_ref_status, data.status);
    }
}
