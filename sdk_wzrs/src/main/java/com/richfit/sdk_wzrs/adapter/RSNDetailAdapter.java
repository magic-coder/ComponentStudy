package com.richfit.sdk_wzrs.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzrs.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by monday on 2017/3/2.
 */

public class RSNDetailAdapter extends CommonTreeAdapter<RefDetailEntity> {

    public RSNDetailAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {

        holder.setText(R.id.rowNum, String.valueOf(position + 1))
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialGroup, item.materialGroup)
                //批次
                .setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag)
                //库存地点
                .setText(R.id.inv, item.invCode)
                //上架仓位
                .setText(R.id.location, item.location)
                //实退数量
                .setText(R.id.quantity, item.quantity);
    }

    public ArrayList<String> getLocations(int position, int flag) {
        ArrayList<String> locations = new ArrayList<>();
        for (int i = 0; i < mVisibleNodes.size(); i++) {
            RefDetailEntity node = mVisibleNodes.get(i);
            if (i == position || TextUtils.isEmpty(node.location) || TextUtils.isEmpty(node.recLocation)) {
                continue;
            }
            locations.add(0 == flag ? node.locationCombine : node.recLocation);
        }
        return locations;
    }

}
