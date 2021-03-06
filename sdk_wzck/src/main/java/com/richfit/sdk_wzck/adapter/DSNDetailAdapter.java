package com.richfit.sdk_wzck.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/2/27.
 */

public class DSNDetailAdapter extends CommonTreeAdapter<RefDetailEntity> {


    public DSNDetailAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.rowNum, String.valueOf(position + 1))
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialGroup, item.materialGroup)
                .setText(R.id.materialUnit, item.unit)
                .setText(R.id.batchFlag, Global.DEFAULT_BATCHFLAG.equalsIgnoreCase(item.batchFlag) ? "" : item.batchFlag)
                .setText(R.id.inv, item.invCode)
                .setText(R.id.location, item.location)
                .setText(R.id.quantity, item.totalQuantity)
                .setText(R.id.specialInvFlag, item.specialInvFlag)
                .setText(R.id.specialInvNum, item.specialInvNum)
                .setText(R.id.tv_location_type, item.locationType);
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