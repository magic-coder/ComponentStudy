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

    private boolean isChangeFirstPos = true;

    public SimpleAdapter(Context context, int layoutId, List<SimpleEntity> datas) {
        super(context, layoutId, datas);
    }

    public SimpleAdapter(Context context, int layoutId, List<SimpleEntity> datas, boolean isChangeFirstPos) {
        super(context, layoutId, datas);
        this.isChangeFirstPos = isChangeFirstPos;
    }

    @Override
    protected void convert(ViewHolder viewHolder, SimpleEntity item, int position) {
        String text = position == 0 ? item.name : item.code + "_" + item.name;

        if (!isChangeFirstPos) {
            text = (item.code + "_" + item.name);
        }
        viewHolder.setText(android.R.id.text1, text);
    }
}
