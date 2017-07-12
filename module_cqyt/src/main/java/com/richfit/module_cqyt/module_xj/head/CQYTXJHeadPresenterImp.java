package com.richfit.module_cqyt.module_xj.head;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.ResultEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTXJHeadPresenterImp extends BaseHeadPresenterImp<CQYTXJHeadContract.View>
        implements CQYTXJHeadContract.Presenter {

    CQYTXJHeadContract.View mView;

    public CQYTXJHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void transferCollectionData(String refNum, String refCodeId, String transId, String bizType, String refType, int inspectionType, String userId, boolean isLocal, String voucherDate,
                                       String transToSapFlag, Map<String, Object> extraHeaderMap) {
        mView = getView();
        addSubscriber(Flowable.concat(uploadInspectedImages(refNum, refCodeId, transId, userId, "01", isLocal),
                mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId,extraHeaderMap))
                .doOnComplete(() -> mRepository.deleteInspectionImages(refNum, refCodeId, isLocal))
                .doOnComplete(() -> FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在结束本次操作...") {

                    @Override
                    public void _onNext(String transNum) {
                        if (mView != null) {
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.closeFail(message);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.closeFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.closeFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.closeComplete();
                        }
                    }
                }));
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
        result.bizPart = "3";
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


}
