package com.richfit.module_mcq.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_tree_rv.MultiItemTypeTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;

import java.util.List;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSDetailAdapter extends MultiItemTypeTreeAdapter<RefDetailEntity> {


    public MCQDSDetailAdapter(Context context, List<RefDetailEntity> allNodes) {
        super(context, allNodes);
    }

}