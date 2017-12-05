package com.richfit.sdk_wzrk.base_asn_detail.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzrk.base_asn_detail.IASNDetailPresenter;
import com.richfit.sdk_wzrk.base_asn_detail.IASNDetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by monday on 2016/11/27.
 */

public class ASNDetailPresenterImp extends BaseDetailPresenterImp<IASNDetailView>
        implements IASNDetailPresenter {

    public ASNDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node,
                         String companyCode, String bizType, String refType, String subFunName, int position) {
        Intent intent = new Intent(mContext, EditActivity.class);
        Bundle bundle = new Bundle();

        //物料
        bundle.putString(Global.EXTRA_MATERIAL_NUM_KEY, node.materialNum);

        bundle.putString(Global.EXTRA_MATERIAL_ID_KEY, node.materialId);

        //入库子菜单类型
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, subFunName + "-明细修改");

        //入库数量
        bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

        //发出库位
        bundle.putString(Global.EXTRA_INV_CODE_KEY, node.invCode);
        bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);

        //上架仓位
        bundle.putString(Global.EXTRA_LOCATION_KEY, node.location);
        //仓位
        bundle.putString(Global.EXTRA_LOCATION_ID_KEY, node.locationId);

        //批次
        bundle.putString(Global.EXTRA_BATCH_FLAG_KEY, node.batchFlag);
        //上架仓位集合
        bundle.putStringArrayList(Global.EXTRA_LOCATION_LIST_KEY, sendLocations);

        intent.putExtras(bundle);

        Activity activity = (Activity) mContext;
        activity.startActivity(intent);
    }


    /**
     * 将服务器返回的三层结构的单据数据，转换成父节点的明细数据
     *
     * @return
     */
    @Override
    protected ArrayList<RefDetailEntity> createNodesByCache(ReferenceEntity refData, ReferenceEntity cache) {
        ArrayList<RefDetailEntity> datas = new ArrayList<>();
        List<RefDetailEntity> billDetailList = cache.billDetailList;
        for (RefDetailEntity lineData : billDetailList) {
            List<LocationInfoEntity> locationList = lineData.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity loc : locationList) {
                    RefDetailEntity data = new RefDetailEntity();
                    //行明细数据
                    data.materialId = lineData.materialId;
                    data.materialNum = lineData.materialNum;
                    data.materialDesc = lineData.materialDesc;
                    data.materialGroup = lineData.materialGroup;
                    data.unit = lineData.unit;
                    data.price = lineData.price;
                    data.totalQuantity = lineData.totalQuantity;
                    data.transLineId = lineData.transLineId;
                    data.invId = lineData.invId;
                    data.invCode = lineData.invCode;
                    //仓位级别数据
                    data.transId = loc.transId;
                    data.location = loc.location;
                    data.batchFlag = loc.batchFlag;
                    data.quantity = loc.quantity;
                    data.recLocation = loc.recLocation;
                    data.recBatchFlag = loc.recBatchFlag;
                    //本位金额(注意行明细的是本位金额的总和)
                    data.money = loc.money;
                    data.locationId = loc.id;
                    data.locationCombine = loc.locationCombine;
                    data.locationType = loc.locationType;
                    datas.add(data);
                }
            }
        }
        Log.e("yff","datas size = " + datas.size());
        return datas;
    }
}
