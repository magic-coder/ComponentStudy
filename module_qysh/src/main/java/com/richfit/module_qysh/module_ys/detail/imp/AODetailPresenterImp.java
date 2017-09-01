package com.richfit.module_qysh.module_ys.detail.imp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_base_sdk.edit.EditActivity;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.module_ys.detail.IAODetailPresenter;
import com.richfit.module_qysh.module_ys.detail.IAODetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/24.
 */

public class AODetailPresenterImp extends BaseDetailPresenterImp<IAODetailView>
        implements IAODetailPresenter {


    public AODetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getReference(ReferenceEntity data, String refNum, String refType, String bizType,
                             String moveType, String refLineId, String userId) {
        mView = getView();

        ResourceSubscriber<ReferenceEntity> subscriber =
                mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .map(refData -> addTreeInfo(refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ReferenceEntity>() {
                            @Override
                            public void onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.showNodes(refData.billDetailList);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.setRefreshing(false, "获取明细失败" + t.getMessage());
                                    //展示抬头获取的数据，没有缓存
                                    mView.showNodes(data.billDetailList);
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.setRefreshing(true, "获取明细成功");
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void deleteNode(String lineDeleteFlag, String refNum, String refLineNum, String refLineId,
                           String refType, String bizType, String userId, int position, String companyCode) {
        mView = getView();

        RxSubscriber<String> subscriber = mRepository.deleteCollectionDataSingle("", "", "", "", refType, bizType, refLineId,
                userId, position, companyCode)
                .doOnNext(str -> mRepository.deleteInspectionImagesSingle(refNum, refLineNum, refLineId, false))
                .doOnNext(str -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, refLineNum, false)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.deleteNodeFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.deleteNodeFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.deleteNodeSuccess(position);
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void editNode(ArrayList<String> sendLocations, ArrayList<String> recLocations,
                         ReferenceEntity refData, RefDetailEntity node, String companyCode,
                         String bizType, String refType, String subFunName, int position) {
        Intent intent = new Intent(mContext, EditActivity.class);
        Bundle bundle = new Bundle();
        //该子节点的id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, node.refLineId);
        //入库子菜单类型
        bundle.putString(Global.EXTRA_COMPANY_CODE_KEY, companyCode);
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);

        //父节点的位置
        bundle.putInt(Global.EXTRA_POSITION_KEY, position);
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, subFunName);

        //库存地点Id
        bundle.putString(Global.EXTRA_INV_ID_KEY, node.invId);
        bundle.putString(Global.EXTRA_INV_CODE_KEY, node.invCode);

        //缓存
        bundle.putString(Global.EXTRA_BATCH_FLAG_KEY, node.batchFlag);
        bundle.putString(Global.EXTRA_TOTAL_QUANTITY_KEY, node.totalQuantity);

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void submitData2BarcodeSystem(String refCodeId, String transId, String bizType,
                                         String refType, String userId, String voucherDate,
                                         String transToSAPFlag, Map<String, Object> extraHeaderMap) {
        mView = getView();
        final String refNum = (String) extraHeaderMap.get("refNum");
        final int inspectionType = (int) extraHeaderMap.get("inspectionType");
        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.submitBarcodeSystemFail("单据号不存在");
            return;
        }
        if ("0".equals(refType)) {
            addSubscriber(Flowable.concat(uploadInspectedImages(refNum, refCodeId, transId, userId, "01", false),
                    mRepository.uploadCollectionData(refCodeId, "", bizType, refType, inspectionType, voucherDate, "", userId, null))
                    .doOnComplete(() -> mRepository.deleteInspectionImages(refNum, refCodeId, false))
                    .doOnComplete(() -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false)))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {

                        @Override
                        public void _onNext(String s) {
                            if (mView != null) {
                                mView.saveMsgFowShow(s);
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
        } else if ("1".equals(refType)) {
            addSubscriber(uploadInspectedImages(refNum, refCodeId, transId, userId, "01", false)
                    .doOnComplete(() -> mRepository.deleteInspectionImages(refNum, refCodeId, false))
                    .doOnComplete(() -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false)))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new ResourceSubscriber<String>() {

                        @Override
                        public void onNext(String s) {
                            if (mView != null) {
                                mView.saveMsgFowShow("图片上传成功!");
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            if (mView != null) {
                                mView.submitBarcodeSystemFail(t.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (mView != null) {
                                mView.submitBarcodeSystemSuccess();
                            }
                        }
                    }));
        }
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
        result.bizHeadId = refCodeId;
        result.bizLineId = image.refLineId;
        result.imageName = image.imageName;
        result.refType = image.refType;
        result.businessType = image.bizType;
        result.bizPart = "1";
        result.imagePath = image.imageDir + File.separator + result.imageName;
        result.createdBy = image.createBy;
        result.imageDate = image.createDate;
        result.userId = userId;
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

}
