package com.richfit.module_qysh.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;

import java.util.List;

/**
 * Created by monday on 2016/11/25.
 */

public class QYSHAOAdapter extends CommonTreeAdapter<RefDetailEntity> {

    public QYSHAOAdapter(Context context, int layoutId,
                         List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }


    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.rowNum, String.valueOf(position + 1));
        holder.setText(R.id.refLineNum, item.lineNum);
        holder.setText(R.id.materialNum, item.materialNum);
        holder.setText(R.id.materialDesc, item.materialDesc);
        //到货数量(相当于应收数量)
        holder.setText(R.id.quantity, item.totalQuantity);
        holder.setText(R.id.work, item.workCode);
        holder.setText(R.id.inv, item.invCode);
        //合同数据(单据数量)
        holder.setText(R.id.orderQuantity, item.orderQuantity);
        //应收数量
        holder.setText(R.id.actQuantity, item.actQuantity);
        holder.setText(R.id.balanceQuantity, item.totalQuantity);

        if (Float.compare(CommonUtil.convertToFloat(item.actQuantity, 0.0F), 0.0F) <= 0) {
            //注意使用setBackgroundColor显示为混合的颜色
            holder.setBackgroundRes(R.id.root_id, R.color.red_200);
        }

        //如果没有过账，但是有缓存数据
        boolean isInspected = !TextUtils.isEmpty(item.totalQuantity) && !"0".equals(item.totalQuantity);
        holder.setText(R.id.isInspected, isInspected ? "已验收" : "未验收");

    }

}