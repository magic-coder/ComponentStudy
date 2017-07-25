package com.richfit.module_xngd.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.adapter.ASNDetailAdapter;

import java.util.List;

/**
 * Created by monday on 2017/7/24.
 */

public class XNGDASNDetailAdapter extends ASNDetailAdapter {

    public XNGDASNDetailAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        super.convert(holder, item, position);
        holder.setText(R.id.xngd_tv_money,item.money);
    }

}
