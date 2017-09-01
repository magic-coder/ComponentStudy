package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.CommonAdapter;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;

import java.util.List;

/**
 * Created by monday on 2017/8/28.
 */

public class ASCXAdapter extends CommonAdapter<RefDetailEntity> {

    public ASCXAdapter(Context context, int layoutId, List<RefDetailEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.mcq_tv_ref_num, item.recordNum)
                .setText(R.id.tv_creator, item.creator)
                .setText(R.id.tv_supplier, item.supplierCode)
                .setText(R.id.work, item.workCode);
    }
}
