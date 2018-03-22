package com.richfit.module_cq.module_ms_313.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/6/30.
 */

public class CQMYS313DetailPresenterImp extends MSDetailPresenterImp {

    public CQMYS313DetailPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node,
                         String companyCode, String bizType, String refType, String subFunName, int position) {

        if (refData != null) {
            TreeNode treeNode = node.getParent();
            if (treeNode != null && RefDetailEntity.class.isInstance(treeNode)) {
                final RefDetailEntity parentNode = (RefDetailEntity) treeNode;
                int indexOf = getParentNodePosition(refData, parentNode.refLineId);
                if (indexOf < 0) {
                    return;
                }
                if (indexOf >= 0 && indexOf < refData.billDetailList.size()) {
                    Intent intent = new Intent(mContext, EditActivity.class);
                    Bundle bundle = new Bundle();
                    //获取该行下所有已经移库的仓位
                    final ArrayList<String> locations = new ArrayList<>();
                    if (parentNode != null) {
                        List<TreeNode> childTreeNodes = parentNode.getChildren();
                        for (TreeNode childTreeNode : childTreeNodes) {
                            if (childTreeNode != null && RefDetailEntity.class.isInstance(childTreeNode)) {
                                final RefDetailEntity childNode = (RefDetailEntity) childTreeNode;
                                if (childNode.getViewType() == Global.CHILD_NODE_HEADER_TYPE || node == childNode)
                                    //排除自己
                                    continue;
                                locations.add(childNode.locationCombine);
                            }
                        }
                    }
                    //该子节点的id
                    bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, node.refLineId);
                    //该子节点的LocationId
                    bundle.putString(Global.EXTRA_LOCATION_ID_KEY, node.locationId);


                    //入库子菜单类型
                    bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
                    bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);
                    //父节点的位置
                    bundle.putInt(Global.EXTRA_POSITION_KEY, indexOf);
                    //入库的子菜单的名称
                    bundle.putString(Global.EXTRA_TITLE_KEY, subFunName + "-明细修改");

                    //该父节点所有的已经入库的仓位
                    bundle.putStringArrayList(Global.EXTRA_LOCATION_LIST_KEY, locations);

                    //库存地点
                    bundle.putString(Global.EXTRA_INV_CODE_KEY, node.invCode);
                    bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);

                    //累计数量
                    bundle.putString(Global.EXTRA_TOTAL_QUANTITY_KEY, parentNode.totalQuantity);

                    //批次
                    bundle.putString(Global.EXTRA_BATCH_FLAG_KEY, node.batchFlag);

                    //接收批次
                    bundle.putString(Global.EXTRA_REC_BATCH_FLAG_KEY,node.recBatchFlag);

                    //上/下架仓位
                    bundle.putString(Global.EXTRA_LOCATION_KEY, node.locationCombine);
                    bundle.putString(Global.EXTRA_SPECIAL_INV_FLAG_KEY, node.specialInvFlag);
                    bundle.putString(Global.EXTRA_SPECIAL_INV_NUM_KEY, node.specialInvNum);

                    //上/下接收仓位
                    bundle.putString(Global.EXTRA_REC_LOCATION_KEY,node.recLocation);

                    //实收数量
                    bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

                    //件数
                    bundle.putString(Global.EXTRA_QUANTITY_CUSTOM_KEY, node.quantityCustom);

                    //仓储类型
                    bundle.putString(Global.EXTRA_LOCATION_TYPE_KEY,node.locationType);

                    intent.putExtras(bundle);

                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                }
            }
        }
    }

}
