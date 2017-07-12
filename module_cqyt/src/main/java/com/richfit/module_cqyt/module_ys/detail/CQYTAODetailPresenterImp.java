package com.richfit.module_cqyt.module_ys.detail;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/6/28.
 */

public class CQYTAODetailPresenterImp extends ASDetailPresenterImp {


    public CQYTAODetailPresenterImp(Context context) {
        super(context);
    }


    @Override
    protected int getParentNodePosition(ReferenceEntity refData, String insLot) {
        int pos = -1;
        if (TextUtils.isEmpty(insLot)) {
            return pos;
        }
        List<RefDetailEntity> list = refData.billDetailList;
        if (list != null && list.size() > 0) {
            for (RefDetailEntity entity : list) {
                ++pos;
                if (insLot.equals(entity.insLot))
                    return pos;
            }
        }
        return pos;
    }

    /**
     * 通过insLot将缓存和原始单据行关联起来
     */
    @Override
    protected RefDetailEntity getLineDataByRefLineId(RefDetailEntity refLineData, ReferenceEntity cachedRefData) {
        if (refLineData == null) {
            return null;
        }
        final String insLot = refLineData.insLot;
        if (TextUtils.isEmpty(insLot))
            return null;
        //通过insLot匹配出缓存中的明细行
        List<RefDetailEntity> detail = cachedRefData.billDetailList;
        for (RefDetailEntity entity : detail) {
            if (insLot.equals(entity.insLot)) {
                return entity;
            }
        }
        return null;
    }

}
