package com.richfit.sdk_wzrk.base_as_detail.imp;

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
import com.richfit.domain.bean.TreeNode;
import com.richfit.sdk_wzrk.base_as_detail.IASDetailPresenter;
import com.richfit.sdk_wzrk.base_as_detail.IASDetailView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 注意这里给出的数据上传是标准的，也就是说uploadCollectionData和上下架是分开的
 * Created by monday on 2016/11/15.
 */

public class ASDetailPresenterImp extends BaseDetailPresenterImp<IASDetailView>
        implements IASDetailPresenter {

    public ASDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node, String companyCode,
                         String bizType, String refType, String subFunName, int position) {
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
                    //获取该行下所有已经上架的仓位
                    final ArrayList<String> locationsOfParentNode = new ArrayList<>();
                    if (parentNode != null) {
                        List<TreeNode> childTreeNodes = parentNode.getChildren();
                        for (TreeNode childTreeNode : childTreeNodes) {
                            if (childTreeNode != null && RefDetailEntity.class.isInstance(childTreeNode)) {
                                final RefDetailEntity childNode = (RefDetailEntity) childTreeNode;
                                if (childNode.getViewType() == Global.CHILD_NODE_HEADER_TYPE || node == childNode)
                                    //排除自己
                                    continue;
                                locationsOfParentNode.add(childNode.location);
                            }
                        }
                    }
                    //该子节点的id
                    bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, parentNode.refLineId);
                    bundle.putString(Global.EXTRA_LOCATION_ID_KEY, node.locationId);

                    //入库子菜单类型
                    bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
                    bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

                    //父节点的位置
                    bundle.putInt(Global.EXTRA_POSITION_KEY, indexOf);

                    //父节点的退货交货数量
                    bundle.putString(Global.EXTRA_RETURN_QUANTITY_KEY, parentNode.returnQuantity);

                    //父节点的项目文本
                    bundle.putString(Global.EXTRA_PROJECT_TEXT_KEY, parentNode.projectText);

                    //移动原因说明
                    bundle.putString(Global.EXTRA_MOVE_CAUSE_DESC_KEY, parentNode.moveCauseDesc);

                    //移动原因
                    bundle.putString(Global.EXTRA_MOVE_CAUSE_KEY, parentNode.moveCause);

                    //决策代码
                    bundle.putString(Global.EXTRA_DECISION_CAUSE_KEY, parentNode.decisionCode);

                    //入库的子菜单的名称
                    bundle.putString(Global.EXTRA_TITLE_KEY, subFunName + "-明细修改");

                    //该父节点所有的已经入库的仓位
                    bundle.putStringArrayList(Global.EXTRA_LOCATION_LIST_KEY, locationsOfParentNode);

                    //库存地点
                    bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);
                    bundle.putString(Global.EXTRA_INV_CODE_KEY, node.invCode);

                    //累计数量
                    bundle.putString(Global.EXTRA_TOTAL_QUANTITY_KEY, parentNode.totalQuantity);

                    //批次
                    bundle.putString(Global.EXTRA_BATCH_FLAG_KEY, node.batchFlag);

                    //上架仓位
                    bundle.putString(Global.EXTRA_LOCATION_KEY, node.location);

                    //仓储类型
                    bundle.putString(Global.EXTRA_LOCATION_TYPE_KEY, node.locationType);

                    //实收数量
                    bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

                    //副计量单位的累计数量
                    bundle.putString(Global.EXTRA_TOTAL_QUANTITY_CUSTOM_KEY, parentNode.totalQuantityCustom);

                    //副计量单位的实收数量
                    bundle.putString(Global.EXTRA_QUANTITY_CUSTOM_KEY, node.quantityCustom);

                    //建议仓位
                    bundle.putString(Global.EXTRA_SUGGEST_LOCATION_KEY, node.suggestLocation);
                    //建议批次
                    bundle.putString(Global.EXTRA_SUGGEST_BATCH_FLAG_KEY, node.suggestBatch);

                    bundle.putString(Global.EXTRA_LOCATION_TYPE_KEY, node.locationType);

                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                }
            }
        }
    }


    /**
     * 通过抬头获取的单据数据和缓存数据生成新的单据数据。
     * 注意我们的目的是将这两部分数据完全分离，这样有利于处理。
     * 注意扩展字段生成的规则：
     * 父节点：原始单据的扩展字段+对应的该行的缓存扩展字段
     * 子节点：该行仓位级别的缓存扩展子弹
     *
     * @param refData：塔头获取的原始单据数据
     * @param cache：缓存单据数据
     * @return
     */
    protected ArrayList<RefDetailEntity> createNodesByCache(ReferenceEntity refData, ReferenceEntity cache) {
        ArrayList<RefDetailEntity> nodes = new ArrayList<>();
        List<RefDetailEntity> list = refData.billDetailList;
        //1.形成父节点数据集合
        for (RefDetailEntity data : list) {
            RefDetailEntity cachedData = getLineDataByRefLineId(data, cache);
            if (cachedData == null) {
                //说明该还没有缓存
                nodes.add(data);
            } else {
                //如果有缓存，那么将缓存作为父节点，注意注意此时应当将原始单据的部分字段信息赋值给缓存。
                //这里我们不适用原始单据信息作为缓存这是因为单据信息是全局的,另外就是修改等针对该节点的操作需要缓存数据
                //将原始单据的物料信息赋值给缓存
                cachedData.lineNum = data.lineNum;
                cachedData.materialNum = data.materialNum;
                cachedData.materialId = data.materialId;
                cachedData.materialDesc = data.materialDesc;
                cachedData.materialGroup = data.materialGroup;
                cachedData.unit = data.unit;
                cachedData.unitCustom = data.unitCustom;
                cachedData.actQuantity = data.actQuantity;
                cachedData.actQuantityCustom = data.actQuantityCustom;
                cachedData.refDoc = data.refDoc;
                cachedData.refDocItem = data.refDocItem;
                cachedData.qmFlag = data.qmFlag;
                cachedData.status = data.status;
                //注意单据中有lineNum105
                cachedData.lineNum105 = data.lineNum105;
                cachedData.insLot = data.insLot;
                cachedData.orderQuantity = data.orderQuantity;
                nodes.add(cachedData);
            }
        }
        //2.形成父节点结构
        addTreeInfo(nodes);
        ArrayList<RefDetailEntity> result = new ArrayList<>();
        result.addAll(nodes);
        //3.生成子节点
        for (RefDetailEntity parentNode : nodes) {
            List<LocationInfoEntity> locationList = parentNode.locationList;
            if (locationList == null || locationList.size() == 0) {
                //说明是原始单据的父节点
                continue;
            }
            //首先去除之前所有父节点的子节点
            parentNode.getChildren().clear();
            parentNode.setHasChild(false);

            for (LocationInfoEntity location : locationList) {
                RefDetailEntity childNode = new RefDetailEntity();
                childNode.refLineId = parentNode.refLineId;
                childNode.invId = parentNode.invId;
                childNode.invCode = parentNode.invCode;
                childNode.totalQuantity = parentNode.totalQuantity;
                childNode.location = location.location;
                childNode.batchFlag = location.batchFlag;
                childNode.quantity = location.quantity;
                childNode.quantityCustom = location.quantityCustom;
                childNode.transId = location.transId;
                childNode.arrivalQuantity = location.arrivalQuantity;
                childNode.transLineId = location.transLineId;
                childNode.locationId = location.id;
                childNode.locationType = location.locationType;
                childNode.money = location.money;
                addTreeInfo(parentNode, childNode, result);
            }
        }
        return result;
    }
}
