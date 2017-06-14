package com.richfit.common_lib.lib_adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_lv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_lv.ViewHolder;
import com.richfit.domain.bean.InvEntity;

import java.util.List;

/**
 * 库存地点Spinner下拉适配器
 */
public class InvAdapter extends CommonAdapter<InvEntity> {

    public InvAdapter(Context context, int layoutId, List<InvEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, InvEntity item, int position) {
        viewHolder.setText(android.R.id.text1, position == 0 ? item.invName :
                item.invName + "_" + item.invCode);
    }
}