package com.richfit.sdk_xxcx.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_xxcx.R;

import java.util.List;

/**
 * 有参考库存查询订单明细适配器
 * Created by monday on 2017/5/25.
 */

public class InvYQueryRefDetailAdapter extends CommonTreeAdapter<RefDetailEntity> {


    public InvYQueryRefDetailAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.lineNum,item.lineNum)
                .setText(R.id.materialNum,item.materialNum)
                .setText(R.id.materialDesc,item.materialDesc)
                .setText(R.id.materialGroup,item.materialGroup)
                .setText(R.id.materialUnit,item.unit)
                .setText(R.id.work,item.workCode)
                .setText(R.id.inv,item.invCode)
                .setText(R.id.orderQuantity,item.orderQuantity);
    }
}
