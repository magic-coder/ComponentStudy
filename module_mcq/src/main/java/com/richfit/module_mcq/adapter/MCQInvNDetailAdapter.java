package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_mcq.R;

import java.util.List;

/**
 * Created by monday on 2017/8/28.
 */

public class MCQInvNDetailAdapter extends CommonTreeAdapter<InventoryEntity> {

    public MCQInvNDetailAdapter(Context context, int layoutId, List<InventoryEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        holder.setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialGroup, item.materialGroup)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialUnit, item.unit)
                .setText(R.id.mcq_material_unit_custom, item.unitCustom)
                .setText(R.id.location, item.location)
                .setText(R.id.invQuantity, item.invQuantity)
                .setText(R.id.mcq_inv_quantity_custom, item.invQuantityCustom);
    }
}