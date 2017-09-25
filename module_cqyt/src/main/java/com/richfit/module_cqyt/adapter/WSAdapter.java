package com.richfit.module_cqyt.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public class WSAdapter extends CommonTreeAdapter<RefDetailEntity> {

    public WSAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setVisible(R.id.batchFlag, false);
        holder.setText(R.id.rowNum, String.valueOf(position + 1))
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialGroup, item.materialGroup)
                //批次
                .setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag)
                .setText(R.id.quantity, item.quantity)
                .setText(R.id.cqyt_tv_declaration_ref, item.declarationRef);

    }

}
