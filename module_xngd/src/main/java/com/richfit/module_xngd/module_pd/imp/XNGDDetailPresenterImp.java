package com.richfit.module_xngd.module_pd.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_xngd.module_pd.XNGDCNEditFragment;
import com.richfit.sdk_wzpd.checkn.detail.imp.CNDetailPresenterImp;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDDetailPresenterImp extends CNDetailPresenterImp{

    public XNGDDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void transferCheckData(String checkId, String userId, String bizType) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.uploadCheckData(checkId, userId, bizType)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            mView.showTransferedNum(s);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_TRANSFER_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.transferCheckDataSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }


    @Override
    public void editNode(InventoryEntity node, String companyCode, String bizType,
                         String refType, String subFunName) {

        Intent intent = new Intent(mContext, EditActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(Global.EXTRA_COMPANY_CODE_KEY, companyCode);
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

        //该子节点的id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, node.checkLineId);
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "物资盘点-明细修改");
        //物料
        bundle.putString(Global.EXTRA_MATERIAL_NUM_KEY, node.materialNum);
        bundle.putString(Global.EXTRA_MATERIAL_ID_KEY, node.materialId);
        //物料描述
        bundle.putString(Global.EXTRA_MATERIAL_DESC_KEY, node.materialDesc);
        //物料组
        bundle.putString(Global.EXTRA_MATERIAL_GROUP_KEY, node.materialGroup);
        //特殊库存标识
        bundle.putString(Global.EXTRA_SPECIAL_INV_FLAG_KEY, node.specialInvFlag);
        bundle.putString(Global.EXTRA_SPECIAL_INV_NUM_KEY, node.specialInvNum);
        //工厂和库存地点
        bundle.putString(Global.EXTRA_WORK_ID_KEY, node.workId);
        bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);
        //库存
        bundle.putString(Global.EXTRA_INV_QUANTITY_KEY, node.invQuantity);
        //需要修改的字段
        bundle.putString(Global.EXTRA_QUANTITY_KEY, node.totalQuantity);
        bundle.putString(Global.EXTRA_LOCATION_KEY, node.location);

        //备注
        bundle.putString(Global.EXTRA_REMARK_KEY,node.remark);

        //物资状态
        bundle.putString(XNGDCNEditFragment.EXTRA_MATERIAL_STATE_KEY,node.materialState);

        intent.putExtras(bundle);

        Activity activity = (Activity) mContext;
        activity.startActivity(intent);
    }
}
