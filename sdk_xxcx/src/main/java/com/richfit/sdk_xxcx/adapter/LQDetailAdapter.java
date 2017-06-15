package com.richfit.sdk_xxcx.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.sdk_xxcx.R;

import java.util.List;

/**
 * Created by monday on 2017/3/17.
 */

public class LQDetailAdapter extends CommonAdapter<InventoryEntity> {

    public LQDetailAdapter(Context context, int layoutId, List<InventoryEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        holder.setText(R.id.work, item.workCode)
                .setText(R.id.inv, item.invCode)
                .setText(R.id.location, item.location)
                .setText(R.id.batchFlag, item.batchFlag)
                .setText(R.id.invQuantity, item.invQuantity);
    }
}
