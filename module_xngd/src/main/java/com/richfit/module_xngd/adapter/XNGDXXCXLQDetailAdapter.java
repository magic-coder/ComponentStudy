package com.richfit.module_xngd.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_xxcx.adapter.LQDetailAdapter;

import java.util.List;

/**
 * Created by monday on 2017/7/19.
 */

public class XNGDXXCXLQDetailAdapter extends LQDetailAdapter {

    public XNGDXXCXLQDetailAdapter(Context context, int layoutId, List<InventoryEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.xngd_tv_inv_flag, item.invFlag)
                .setText(R.id.xngd_tv_project_flag, item.projectFlag)
                .setText(R.id.xngd_tv_project_num, item.projectNum)
                .setText(R.id.invType, item.invType);
    }
}
