package com.richfit.module_qhyt.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;

import java.util.ArrayList;
import java.util.List;

public class QHYTAS103DetailAdapter extends CommonTreeAdapter<RefDetailEntity> {


    public QHYTAS103DetailAdapter(Context context, int layoutId, List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity data, int position) {
        holder.setText(R.id.lineNum, data.lineNum)
                //物料号
                .setText(R.id.materialNum, data.materialNum)
                //物资描述
                .setText(R.id.materialDesc, data.materialDesc)
                //物料组
                .setText(R.id.materialGroup, data.materialGroup)
                //计量单位
                .setText(R.id.materialUnit, data.unit)
                //特殊库存标识
                .setText(R.id.specialInventoryFlag, data.specialInvFlag)
                //应收数量
                .setText(R.id.actQuantity, data.actQuantity)
                //累计实收数量
                .setText(R.id.totalQuantity, data.totalQuantity)
                //工厂
                .setText(R.id.work, data.workCode)
                //库存地点
                .setText(R.id.inv, data.invCode);
    }

    /**
     * 给出同一级别的其他节点中所有的仓位信息
     * @param position
     * @param flag : 0:表示仓位/发出仓位 其他表示接受仓位
     * @return
     */
    public ArrayList<String> getLocations(int position, int flag) {
        ArrayList<String> locations = new ArrayList<>();
        for (int i = 0; i < mVisibleNodes.size(); i++) {
            RefDetailEntity node = mVisibleNodes.get(i);
            if (i == position || TextUtils.isEmpty(node.location) || TextUtils.isEmpty(node.recLocation))
                continue;
            locations.add(flag == 0 ? node.location : node.recLocation);
        }
        return locations;
    }

}