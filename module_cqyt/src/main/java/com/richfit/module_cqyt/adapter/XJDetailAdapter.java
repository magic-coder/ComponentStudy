package com.richfit.module_cqyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.module_cqyt.R;

import java.util.List;

/**
 * Created by monday on 2017/7/4.
 */

public class XJDetailAdapter extends CommonAdapter<LocationInfoEntity> {


    public XJDetailAdapter(Context context, int layoutId, List<LocationInfoEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LocationInfoEntity item, int position) {
        holder.setText(R.id.show_title, item.location)
                .setText(R.id.show_time, item.creationDate);
        //处理横轴线
        if(position % 3 == 0) {
            //画三次换行

        }
    }
}
