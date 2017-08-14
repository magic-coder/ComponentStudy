package com.richfit.common_lib.lib_adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_lv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_lv.ViewHolder;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;

/**
 * 该适配器用来给Spinner装载数据
 * Created by monday on 2017/8/15.
 */

public class SimpleAdapter extends CommonAdapter<SimpleEntity> {

    public SimpleAdapter(Context context, int layoutId, List<SimpleEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, SimpleEntity item, int position) {
        viewHolder.setText(android.R.id.text1, position == 0 ? item.name : item.code + "_" + item.name);
    }
}
