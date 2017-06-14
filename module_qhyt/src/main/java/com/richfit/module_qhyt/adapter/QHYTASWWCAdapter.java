package com.richfit.module_qhyt.adapter;

import android.content.Context;
import android.util.Log;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;

import java.util.List;

/**
 * 青海委外组件适配器
 * Created by monday on 2017/3/10.
 */

public class QHYTASWWCAdapter extends CommonTreeAdapter<RefDetailEntity> {

    public QHYTASWWCAdapter(Context context, int layoutId,
                            List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.refLineNum, String.valueOf(item.refDocItem))
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.batchFlag, item.batchFlag)
                .setText(R.id.totalUsedQuantity, item.totalQuantity);
    }

}
