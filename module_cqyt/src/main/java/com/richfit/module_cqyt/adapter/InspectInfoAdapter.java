package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_lv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_lv.ViewHolder;
import com.richfit.domain.bean.InventoryEntity;

import java.util.List;

/**
 * 报检单
 * Created by monday on 2017/9/26.
 */

public class InspectInfoAdapter extends CommonAdapter<InventoryEntity>{

    public InspectInfoAdapter(Context context, int layoutId, List<InventoryEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, InventoryEntity item, int position) {
        viewHolder.setText(android.R.id.text1,item.inspectionNum);
    }
}
