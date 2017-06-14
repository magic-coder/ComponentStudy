package com.richfit.sdk_wzpd.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.sdk_wzpd.R;

import java.util.List;

/**
 * Created by monday on 2017/3/6.
 */

public class BlindDetailAdapter extends CommonTreeAdapter<InventoryEntity> {


    public BlindDetailAdapter(Context context, int layoutId, List<InventoryEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        holder.setText(R.id.rowNum, String.valueOf(position + 1))
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialGroup, item.materialGroup)
                .setText(R.id.checkLocation, item.location)
                .setText(R.id.checkQuantity, item.totalQuantity);
    }

}
