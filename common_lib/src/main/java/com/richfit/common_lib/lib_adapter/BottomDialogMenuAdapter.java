package com.richfit.common_lib.lib_adapter;

import android.content.Context;

import com.richfit.common_lib.R;
import com.richfit.common_lib.lib_adapter_lv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_lv.ViewHolder;
import com.richfit.domain.bean.BottomMenuEntity;

import java.util.List;


/**
 * 底部公共功能菜单适配器
 */
public class BottomDialogMenuAdapter extends CommonAdapter<BottomMenuEntity> {

    public BottomDialogMenuAdapter(Context context, int layoutId, List<BottomMenuEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, BottomMenuEntity item, int position) {
        viewHolder.setText(R.id.menu_tv,mDatas.get(position).menuName);
        viewHolder.setImageResource(R.id.menu_iv,mDatas.get(position).menuImageRes);
    }
}
