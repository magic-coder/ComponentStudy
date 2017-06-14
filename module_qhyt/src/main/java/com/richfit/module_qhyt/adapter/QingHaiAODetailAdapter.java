package com.richfit.module_qhyt.adapter;

import android.content.Context;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_tree_rv.CommonTreeAdapter;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;

import java.util.List;

/**
 * Created by monday on 2017/2/28.
 */

public class QingHaiAODetailAdapter extends CommonTreeAdapter<RefDetailEntity> {



    public QingHaiAODetailAdapter(Context context, int layoutId,
                                  List<RefDetailEntity> allNodes) {
        super(context, layoutId, allNodes);
    }

    @Override
    protected void convert(ViewHolder holder, RefDetailEntity item, int position) {
        holder.setText(R.id.rowNum, String.valueOf(position + 1))
                .setText(R.id.refLineNum,item.lineNum)
                .setText(R.id.specialInvFlag,item.specialInvFlag)
                .setText(R.id.materialNum, item.materialNum)
                .setText(R.id.materialDesc, item.materialDesc)
                .setText(R.id.materialGroup, item.materialGroup)
                .setText(R.id.materialUnit,item.unit)
                .setText(R.id.actQuantity,item.actQuantity)
                .setText(R.id.quantity, item.totalQuantity)
                .setText(R.id.inv, item.invCode)
                .setText(R.id.manufacturer,item.manufacturer)
                .setText(R.id.randomQuantity,item.randomQuantity)
                .setText(R.id.qualifiedQuantity,item.qualifiedQuantity)
                .setText(R.id.rustQuantity,item.rustQuantity)
                .setText(R.id.damagedQuantity,item.damagedQuantity)
                .setText(R.id.badQuantity,item.badQuantity)
                .setText(R.id.otherQuantity,item.otherQuantity)
                .setText(R.id.zPackage,item.sapPackage)
                .setText(R.id.qmNum,item.qmNum)
                .setText(R.id.certificate,item.certificate)
                .setText(R.id.manual,item.instructions)
                .setText(R.id.inspectionQuantity,item.inspectionQuantity)
                .setText(R.id.qmCertificate,item.qmCertificate)
                .setText(R.id.claimNum,item.claimNum)
                .setText(R.id.inspectionResult,item.inspectionResult)
                .setText(R.id.remark,item.remark);
    }

}
