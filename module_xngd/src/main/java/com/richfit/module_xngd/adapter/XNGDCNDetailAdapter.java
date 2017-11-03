package com.richfit.module_xngd.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzpd.adapter.CNDetailAdapter;

import java.util.List;

/**
 * Created by monday on 2017/10/31.
 */

public class XNGDCNDetailAdapter extends CNDetailAdapter {

    public XNGDCNDetailAdapter(Context context, int layoutId, List<InventoryEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.xngd_tv_material_state, item.materialState)
                .setText(R.id.remark, item.remark);
    }
}
