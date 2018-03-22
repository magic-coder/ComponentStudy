package com.richfit.module_cq.module_as101.impl;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2018/1/29.
 */

public class CQAS101DetailPresenterImpl extends ASDetailPresenterImp {
    public CQAS101DetailPresenterImpl(Context context) {
        super(context);
    }


    @Override
    public void submitData2BarcodeSystem(String refCodeId, String transId, String bizType,
                                         String refType, String userId, String voucherDate,
                                         String transToSap, Map<String, Object> extraHeaderMap) {
        mView = getView();
        String refNum = null;
        if(extraHeaderMap != null) {
            refNum  = (String) extraHeaderMap.get("refNum");
        }
        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.submitBarcodeSystemFail("单据号不存在");
            return;
        }
        RxSubscriber<String> subscriber =
                Flowable.concat(
                uploadInspectedImages(refNum, refCodeId, transId, userId, "01", false),
                mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId, extraHeaderMap),
                mRepository.transferCollectionData(transId, bizType, refType, userId, voucherDate, transToSap, extraHeaderMap))
                .doOnError(str -> saveData2Spre(bizType,refType,"0"))
                .doOnComplete(() -> saveData2Spre(bizType,refType,"1"))
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
        result.bizHeadId = refCodeId;
        result.bizLineId = image.refLineId;
        result.imageName = image.imageName;
        result.refType   = image.refType;
        result.businessType = image.bizType;
        result.bizPart   = "2";
        result.imagePath = image.imageDir + File.separator + result.imageName;
        result.createdBy = image.createBy;
        result.imageDate = image.createDate;
        result.userId = userId;
        result.fileType = image.takePhotoType;
        result.transFileToServer = transFileToServer;
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
