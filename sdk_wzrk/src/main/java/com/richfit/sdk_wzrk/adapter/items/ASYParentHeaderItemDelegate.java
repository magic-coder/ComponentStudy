package com.richfit.sdk_wzrk.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzrk.R;

/**
 * 标准物资出库父节点抬头的Item代理
 * Created by monday on 2017/3/17.
 */

public class ASYParentHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.wzrk_item_asy_detail_parent_header;
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
                //特殊库存标识
                .setText(R.id.specialInventoryFlag, data.specialInvFlag)
                //应收数量
                .setText(R.id.actQuantity, data.actQuantity)
                //累计实收数量
                .setText(R.id.totalQuantity, data.totalQuantity)
                //工厂
                .setText(R.id.work, data.workCode)
                //库存地点
                .setText(R.id.inv, data.invCode);
    }
}
