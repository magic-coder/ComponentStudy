package com.richfit.module_cqyt.module_ms.ubsto.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoDetailPresenterImp extends DSDetailPresenterImp {


    public CQYTUbstoDetailPresenterImp(Context context) {
        super(context);
    }


    /**
     * 保存图片+上传数据
     *
     * @param refCodeId
     * @param transId
     * @param bizType
     * @param refType
     * @param userId
     * @param voucherDate
     * @param transToSapFlag
     * @param extraHeaderMap
     */
    @Override
    public void submitData2BarcodeSystem(String refCodeId, String transId, String bizType, String refType, String userId, String voucherDate,
                                         String transToSapFlag, Map<String, Object> extraHeaderMap) {
        if (extraHeaderMap != null) {
            final String refNum = CommonUtil.Obj2String(extraHeaderMap.get("refNum"));
            addSubscriber(Flowable.concat(uploadInspectedImages(refNum, refCodeId, transId, userId, "01", false),
                    mRepository.uploadCollectionData("", transId, bizType, refType, -1, voucherDate, "", "", extraHeaderMap))
                    .doOnError(str -> SPrefUtil.saveData(bizType + refType, "0"))
                    .doOnComplete(() -> mRepository.deleteInspectionImages(refNum, refCodeId, false))
                    .doOnComplete(() -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false)))
                    .doOnComplete(() -> SPrefUtil.saveData(bizType + refType, "1"))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new RxSubscriber<String>(mContext, "正在保存数据...") {

                        @Override
                        public void _onNext(String transNum) {
                            if (mView != null) {
                                mView.saveMsgFowShow(transNum);
                            }
                        }

                        @Override
                        public void _onNetWorkConnectError(String message) {
                            if (mView != null) {
                                mView.submitBarcodeSystemFail(message);
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
                    }));
        }
    }

    /**
     * 05
     *
     * @param transId
     * @param bizType
     * @param refType
     * @param userId
     * @param voucherDate
     * @param transToSapFlag
     * @param extraHeaderMap
     */
    @Override
    public void submitData2SAP(String transId, String bizType, String refType, String userId,
                               String voucherDate, String transToSapFlag, Map<String, Object> extraHeaderMap) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.transferCollectionData(transId, bizType, refType, userId,
                voucherDate, transToSapFlag, extraHeaderMap)
                .doOnError(str -> SPrefUtil.saveData(bizType + refType, "1"))
                .doOnComplete(() -> SPrefUtil.saveData(bizType + refType, "2"))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.saveMsgFowShow(message);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_UPLOAD_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.submitSAPFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.submitSAPFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.submitSAPSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }


    /**
     * 06
     *
     * @param transId
     * @param bizType
     * @param refType
     * @param userId
     * @param voucherDate
     * @param transToSapFlag
     * @param extraHeaderMap
     * @param submitFlag
     */
    @Override
    public void sapUpAndDownLocation(String transId, String bizType, String refType, String userId,
                                     String voucherDate, String transToSapFlag,
                                     Map<String, Object> extraHeaderMap, int submitFlag) {
        mView = getView();
        RxSubscriber<String> subscriber =
                mRepository.transferCollectionData(transId, bizType, refType, userId, voucherDate,
                        transToSapFlag, extraHeaderMap)
                        .compose(TransformerHelper.io2main())
                        .doOnError(str -> SPrefUtil.saveData(bizType + refType, "2"))
                        .doOnComplete(() -> SPrefUtil.saveData(bizType + refType, "0"))
                        .subscribeWith(new RxSubscriber<String>(mContext, "正在下架...") {
                            @Override
                            public void _onNext(String message) {
                                if (mView != null) {
                                    mView.saveMsgFowShow(message);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(message);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.upAndDownLocationFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.upAndDownLocationFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.upAndDownLocationSuccess();
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
        result.bizPart = "2";
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
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> refLocations,
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
                    //获取该行下所有已经上架的仓位
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
                    //累计数量
                    bundle.putSerializable(Global.EXTRA_TOTAL_QUANTITY_KEY, node.totalQuantity);
                    //需要修改的字段
                    //上架仓位
                    bundle.putString(Global.EXTRA_LOCATION_KEY, node.locationCombine);
                    bundle.putString(Global.EXTRA_SPECIAL_INV_FLAG_KEY, node.specialInvFlag);
                    bundle.putString(Global.EXTRA_SPECIAL_INV_NUM_KEY, node.specialInvNum);
                    //实收数量
                    bundle.putString(Global.EXTRA_QUANTITY_KEY, node.quantity);

                    //件数
                    bundle.putString(Global.EXTRA_QUANTITY_CUSTOM_KEY, node.quantityCustom);

                    bundle.putString(Global.EXTRA_LOCATION_TYPE_KEY,node.locationType);

                    intent.putExtras(bundle);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                }
            }
        }
    }
}
