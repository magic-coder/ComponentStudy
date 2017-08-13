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
        //应急物资
        holder.setText(R.id.xngd_tv_inv_flag, item.invFlag)
                //库存类型
                .setText(R.id.specialInvFlag, item.specialInvFlag)
                //项目移交物资
                .setText(R.id.xngd_tv_inv_type, item.invType)
                //项目编号
                .setText(R.id.xngd_tv_project_num, item.projectNum);
    }
}
