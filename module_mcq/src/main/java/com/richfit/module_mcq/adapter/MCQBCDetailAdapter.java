package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_mcq.R;

import java.util.List;

/**
 * Created by monday on 2017/8/29.
 */

public class MCQBCDetailAdapter extends CommonTreeAdapter<InventoryEntity> {

    public MCQBCDetailAdapter(Context context, int layoutId, List<InventoryEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        holder.setText(R.id.location, item.location)
                .setText(R.id.quantity, item.totalQuantity)
                .setText(R.id.mcq_quantity_custom, item.totalQuantityCustom)
                .setText(R.id.mcq_creation_date, item.creationDate)
                .setText(R.id.mcq_year, item.year)
                .setText(R.id.mcq_storage_num, item.storageNum)
                .setText(R.id.mcq_duration, item.duration);
    }

}
