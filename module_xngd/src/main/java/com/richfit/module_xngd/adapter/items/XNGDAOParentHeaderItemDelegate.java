package com.richfit.module_xngd.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ItemViewDelegate;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAOParentHeaderItemDelegate implements ItemViewDelegate<RefDetailEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.xngd_item_detail_parent_header;
    }

    @Override
    public boolean isForViewType(RefDetailEntity item, int position) {
        return item.getViewType() == Global.PARENT_NODE_HEADER_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, RefDetailEntity data, int position) {
        holder.setText(R.id.refLineNum, data.lineNum)
                .setText(R.id.materialNum, data.materialNum)
                .setText(R.id.materialDesc, data.materialDesc)
                .setText(R.id.materialGroup, data.materialGroup)
                .setText(R.id.materialUnit,data.unit)
                .setText(R.id.specialInvFlag, data.specialInvFlag)
                .setText(R.id.orderQuantity, data.orderQuantity)
                .setText(R.id.actQuantity, data.actQuantity)
                .setText(R.id.totalQuantity, data.totalQuantity)
                .setText(R.id.xngd_allQuantity, data.allQuantity)
                .setText(R.id.xngd_partQuantity, data.partQuantity)
                //检验方法
                .setText(R.id.xngd_inspectionType, data.inspectionTypeName)
                //检验状况
                .setText(R.id.xngd_inspectionStatus, data.inspectionStatusName)
                //处理情况
                .setText(R.id.xngd_processResult, data.processResult)
                .setText(R.id.work, data.workCode)
                .setText(R.id.inv, data.invCode);
    }

}
