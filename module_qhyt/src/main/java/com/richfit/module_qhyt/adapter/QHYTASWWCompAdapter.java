package com.richfit.module_qhyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_lv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_lv.ViewHolder;
import com.richfit.domain.bean.InventoryEntity;

import java.util.List;

/**
 * 青海委外入库组件批次的适配器
 * Created by monday on 2017/3/17.
 */

public class QHYTASWWCompAdapter extends CommonAdapter<InventoryEntity> {
    public QHYTASWWCompAdapter(Context context, int layoutId, List<InventoryEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, InventoryEntity item, int position) {
        holder.setText(android.R.id.text1,item.batchFlag);
    }
}
