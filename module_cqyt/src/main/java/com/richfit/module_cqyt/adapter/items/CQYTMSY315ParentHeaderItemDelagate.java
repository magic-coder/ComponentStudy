package com.richfit.module_cqyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

/**
 * Created by monday on 2017/7/11.
 */

public class CQYTMSY315ParentHeaderItemDelagate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.cqyt_item_msy315_detail_parent_head;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity data, int position) {
        holder.setText(R.id.lineNum, data.lineNum)
                //参考单据
                .setText(R.id.lineNum105, data.lineNum105)
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
                //累计件数
                .setText(R.id.cqyt_tv_total_quantity_custom, data.totalQuantityCustom)
                //工厂
                .setText(R.id.work, data.workCode)
                //库存地点
                .setText(R.id.inv, data.invCode);
    }
}
