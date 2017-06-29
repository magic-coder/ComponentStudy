package com.richfit.module_xngd.module_ys.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.module_xngd.module_ys.XNGDAOEditFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/6/28.
 */

public class XNGDAODetailPresenterImp extends ASDetailPresenterImp
        implements IXNGDAODetailPresenter {


    public XNGDAODetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void transferCollectionData(String refNum, String refCodeId, String transId, String bizType, String refType, int inspectionType, String userId, boolean isLocal, String voucherDate, String transToSapFlag, Map<String, Object> extraHeaderMap) {
        mView = getView();

        RxSubscriber<String> subscriber = Flowable.concat(uploadInspectedImages(refNum, refCodeId, transId, userId, "01", isLocal),
                mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId))
                .doOnComplete(() -> mRepository.deleteInspectionImages(refNum, refCodeId, isLocal))
                .doOnComplete(() -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {

                    @Override
                    public void _onNext(String transNum) {
                        if (mView != null) {
                            mView.showTransferedVisa(transNum);
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
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.submitBarcodeSystemSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);

    }

    /**
     * 上传图片。如果没有图片直接过滤掉。
     */
    private Flowable<String> uploadInspectedImages(String refNum, String refCodeId, String transId,
                                                   String userId, String transFileToServer, boolean isLocal) {


        return Flowable.just(refNum)
                .map(num -> mRepository.readImagesByRefNum(refNum, isLocal))
                .filter(images -> images != null && images.size() > 0)
                .flatMap(images -> Flowable.fromIterable(wrapperImage(images, refCodeId, transId, userId, transFileToServer)))
                .buffer(3)
                .flatMap(results -> mRepository.uploadMultiFiles(results));
    }

    /**
     * 图片上传。将imageEntity装换成上传ResultEntity实体类
     *
     * @return
     */
    private ResultEntity wrapperImageInternal(ImageEntity image, String refCodeId, String transId, String userId,
                                              String transFileToServer) {
        ResultEntity result = new ResultEntity();
        result.suffix = Global.IMAGE_DEFAULT_FORMAT;
        result.bizHeadId = transId;
        result.bizLineId = image.refLineId;
        result.imageName = image.imageName;
        result.refType = image.refType;
        result.businessType = image.bizType;
        result.bizPart = "1";
        result.imagePath = image.imageDir + File.separator + result.imageName;
        result.createdBy = image.createBy;
        result.imageDate = image.createDate;
        result.userId = userId;
        result.transFileToServer = transFileToServer;
        result.fileType = image.takePhotoType;
        return result;
    }

    private ArrayList<ResultEntity> wrapperImage(List<ImageEntity> images, String refCodeId, String transId, String userId,
                                                 String transFileToServer) {
        ArrayList<ResultEntity> results = new ArrayList<>();
        int pos = -1;
        for (ImageEntity image : images) {
            ResultEntity result = wrapperImageInternal(image, refCodeId, transId, userId, transFileToServer);
            result.taskId = ++pos;
            results.add(result);
        }
        return results;
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

                    //实收数量
                    bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

                    //全检数量
                    bundle.putString(XNGDAOEditFragment.EXTRA_ALL_QUANTITY_KEY, parentNode.allQuantity);

                    //抽检数量
                    bundle.putString(XNGDAOEditFragment.EXTRA_PART_QUANTITY_KEY, parentNode.partQuantity);

                    //检验方法
                    bundle.putString(XNGDAOEditFragment.EXTRA_INSPECTION_TYPE_KEY, parentNode.inspectionType);

                    //检验状况
                    bundle.putString(XNGDAOEditFragment.EXTRA_INSPECTION_STATUS_KEY, parentNode.inspectionStatus);

                    //处理结果
                    bundle.putString(XNGDAOEditFragment.EXTRA_PROCESS_RESULT_KEY, parentNode.processResult);

                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                }
            }
        }
    }
}
