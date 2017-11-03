package com.richfit.sdk_wzrs.base_rsn_detail.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzrs.base_rsn_detail.IRSNDetailPresenter;
import com.richfit.sdk_wzrs.base_rsn_detail.IRSNDetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/3/2.
 */

public class RSNDetailPresenterImp extends BaseDetailPresenterImp<IRSNDetailView>
        implements IRSNDetailPresenter {


    public RSNDetailPresenterImp(Context context) {
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

        //退库数量
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
    protected ArrayList<RefDetailEntity> createNodesByCache(ReferenceEntity refData, ReferenceEntity cache)  {
        ArrayList<RefDetailEntity> datas = new ArrayList<>();
        List<RefDetailEntity> billDetailList = cache.billDetailList;
        for (RefDetailEntity target : billDetailList) {
            List<LocationInfoEntity> locationList = target.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity loc : locationList) {
                    RefDetailEntity data = new RefDetailEntity();
                    //父节点的数据
                    data.materialId = target.materialId;
                    data.materialNum = target.materialNum;
                    data.materialDesc = target.materialDesc;
                    data.materialGroup = target.materialGroup;
                    data.unit = target.unit;
                    data.price = target.price;
                    data.totalQuantity = target.totalQuantity;
                    data.transLineId = target.transLineId;
                    data.invId = target.invId;
                    data.invCode = target.invCode;
                    //子节点的数据
                    data.transId = loc.transId;
                    data.location = loc.location;
                    data.batchFlag = loc.batchFlag;
                    data.quantity = loc.quantity;
                    data.recLocation = loc.recLocation;
                    data.recBatchFlag = loc.recBatchFlag;
                    data.locationId = loc.id;
                    data.specialInvFlag = loc.specialInvFlag;
                    data.specialInvNum = loc.specialInvNum;
                    data.locationCombine = loc.locationCombine;
                    datas.add(data);
                }
            }
        }
        return datas;
    }
}
