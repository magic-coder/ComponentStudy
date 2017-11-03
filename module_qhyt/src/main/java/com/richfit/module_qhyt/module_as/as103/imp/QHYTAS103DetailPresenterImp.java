package com.richfit.module_qhyt.module_as.as103.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by monday on 2017/2/17.
 */

public class QHYTAS103DetailPresenterImp extends ASDetailPresenterImp {

    public QHYTAS103DetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node, String companyCode,
                         String bizType, String refType, String subFunName, int position) {

        Intent intent = new Intent(mContext, EditActivity.class);
        Bundle bundle = new Bundle();
        //该子节点的id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, node.refLineId);
        bundle.putString(Global.EXTRA_LOCATION_ID_KEY, node.locationId);

        //入库子菜单类型
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);

        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

        //父节点的位置
        bundle.putInt(Global.EXTRA_POSITION_KEY, position);

        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, subFunName + "-明细修改");

        //库存地点
        bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);
        bundle.putString(Global.EXTRA_INV_CODE_KEY, node.invCode);

        //累计数量
        bundle.putString(Global.EXTRA_TOTAL_QUANTITY_KEY, node.totalQuantity);

        //批次
        bundle.putString(Global.EXTRA_BATCH_FLAG_KEY, node.batchFlag);

        //上架仓位
        bundle.putString(Global.EXTRA_LOCATION_KEY, node.location);

        //实收数量
        bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

        //发出仓位集合
        bundle.putStringArrayList(Global.EXTRA_LOCATION_LIST_KEY, sendLocations);

        intent.putExtras(bundle);
        Activity activity = (Activity) mContext;
        activity.startActivity(intent);
    }

    /**
     * 对于103入库，有参考但是不是父子节点的结构，所以需要重写createNodesByCache
     * 方法
     *
     * @param refData：塔头获取的原始单据数据
     * @param cache：缓存单据数据
     * @return
     */
    @Override
    protected ArrayList<RefDetailEntity> createNodesByCache(ReferenceEntity refData, ReferenceEntity cache) {
        ArrayList<RefDetailEntity> nodes = new ArrayList<>();
        //第一步，将原始单据中的行明细赋值新的父节点中
        List<RefDetailEntity> list = refData.billDetailList;
        for (RefDetailEntity node : list) {
            //获取缓存中的明细，如果该行明细没有缓存，那么该行明细仅仅赋值原始单据信息
            RefDetailEntity cachedEntity = getLineDataByRefLineId(node, cache);
            if (cachedEntity == null) {
                cachedEntity = node;
            }
            cachedEntity.lineNum = node.lineNum;
            cachedEntity.materialNum = node.materialNum;
            cachedEntity.materialId = node.materialId;
            cachedEntity.materialDesc = node.materialDesc;
            cachedEntity.materialGroup = node.materialGroup;
            cachedEntity.actQuantity = node.actQuantity;
            cachedEntity.workCode = node.workCode;
            cachedEntity.batchFlag = node.batchFlag;
            //将仓位级别的数据保存到明细行级别中
            List<LocationInfoEntity> locationList = cachedEntity.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity loc : locationList) {
                    //仓位级别的数据
                    cachedEntity.transId = loc.transId;
                    cachedEntity.location = loc.location;
                    cachedEntity.quantity = loc.quantity;
                    cachedEntity.transLineId = loc.transLineId;
                    cachedEntity.locationId = loc.id;
                    cachedEntity.batchFlag = loc.batchFlag;
                }
            }
            nodes.add(cachedEntity);
        }
        return nodes;
    }


}
