package com.richfit.module_qhyt.adapter.items;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrk.adapter.items.ASYParentHeaderItemDelegate;

public class QHYTAS105ParentHeaderItemDelegate extends ASYParentHeaderItemDelegate {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.qhyt_item_as105_parent_header;
    }


    @Override
    public void convert(ViewHolder holder, RefDetailEntity data, int position) {
        //绑定标志入库的数据
        super.convert(holder, data, position);
        //105必检客户化字段
        holder//检验批
                .setText(R.id.insLot, data.insLot)
                //物料凭证
                .setText(R.id.refDoc, data.refDoc)
                //物料凭证行号
                .setText(R.id.refDocItem, String.valueOf(data.refDocItem))
                //退货交货数量
                .setText(R.id.returnDeliveryQuantity, data.returnQuantity)
                //项目文本
                .setText(R.id.projectText, data.projectText)
                //移动原因说明
                .setText(R.id.moveCauseDesc, data.moveCauseDesc)
                //移动原因
                .setText(R.id.moveCause, data.moveCause)
                //决策代码
                .setText(R.id.strategyCode, data.decisionCode);
    }
}
