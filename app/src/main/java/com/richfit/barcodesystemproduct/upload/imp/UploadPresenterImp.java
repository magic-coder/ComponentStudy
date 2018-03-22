
package com.richfit.barcodesystemproduct.upload.imp;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.barcodesystemproduct.R;
import com.richfit.barcodesystemproduct.upload.UploadContract;
import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.FileUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ImageEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.UploadMsgEntity;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 出入库(包括验收)业务的离线上传:
 * 1. 先从本地数据库将需要的数据读取出来，由于读取出来是单据的三层结构，所以先对数据进行加工.
 * 注意需要保存每一个单据的抬头信息，这样有利于用户点击错误明细直接修改;
 * 2. 数据加工成List<ResultEntity>的形式时候开始上服务器传输;
 * 3. 直接调用transCollectedDataOffLine接口上传数据,成功返回后，通过业务类型进行上下架处理(验收需要
 * 上传图片).
 * 4. transCollectedDataOffLine成功后，直接调用setTransFlag将缓存标识设置成3，不允许在删除；
 * Created by monday on 2017/4/17.
 */

public class UploadPresenterImp extends BaseDetailPresenterImp<UploadContract.View>
        implements UploadContract.Presenter {

    protected static final String BIZTYPE_DESC_KEY = "bizTypeDesc";
    protected static final String REFTYPE_DESC_KEY = "refTypeDesc";
    protected static final String TRANSTOSAPFLAG_KEY = "transTosapFlag";

    /*注意这里是从-1开始*/
    protected int mTaskNum = 0;
    UploadContract.View mView;
    List<ReferenceEntity> mRefDatas;
    /*返回给用户的信息*/
    SparseArray<UploadMsgEntity> mMessageArray;
    /*过账额外的字段信息*/
    HashMap<String, Object> mExtraTransMap;
    /*需要回传的总数*/
    int mTotalUploadDataNum;
    UploadMsgEntity info;

    public UploadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        mRefDatas = new ArrayList<>();
        mMessageArray = new SparseArray<>();
        mExtraTransMap = new HashMap<>();
    }

    @Override
    public void readUploadData() {
        mView = getView();
        mRefDatas.clear();
        mMessageArray.clear();
        mExtraTransMap.clear();
        mTotalUploadDataNum = 0;
        ResourceSubscriber<ArrayList<ResultEntity>> subscriber =
                mRepository.readTransferedData(1)
                        .filter(list -> list != null && list.size() > 0)
                        .map(list -> calcTotalNum(list))
                        .flatMap(list -> Flowable.fromIterable(list))
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .map(refData -> wrapper2Results(refData, true))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<ResultEntity>>() {

                            @Override
                            protected void onStart() {
                                super.onStart();
                                if (mView != null) {
                                    mView.startReadUploadData();
                                }
                            }

                            @Override
                            public void onNext(ArrayList<ResultEntity> results) {
                                if (mView != null) {
                                    mView.showUploadData(results);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.readUploadDataFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.readUploadDataComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    /**
     * 上传出入库离线数据
     */
    @Override
    public void uploadCollectedDataOffLine() {
        mView = getView();
        mTaskNum = 0;
        info = null;
        if (mRefDatas != null && mRefDatas.size() == 0) {
            mView.uploadCollectDataComplete();
            return;
        }
        ResourceSubscriber<String> subscriber =
                Flowable.fromIterable(mRefDatas)
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .map(refData -> wrapper2Results(refData, false))
                        .map(lines -> insertImgNames(lines))
                        .flatMap(results -> mRepository.uploadCollectionDataOffline(results))
                        .flatMap(message -> submitData2SAPInner(message))
                        .doOnComplete(() -> mRepository.deleteOfflineDataAfterUploadSuccess("", "0", "", ""))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext, false) {
                            @Override
                            protected void onStart() {
                                super.onStart();
                                if (mView != null) {
                                    mView.startUploadData(mTotalUploadDataNum);
                                }
                            }

                            @Override
                            public void _onNext(String transNum) {
                                mMessageArray.get(mTaskNum).taskId = mTaskNum;
                                UploadMsgEntity info = mMessageArray.get(mTaskNum);
                                if (mView != null && info != null) {
                                    info.transNum = transNum;
                                    mView.uploadCollectDataSuccess(info);
                                    L.e("onNext mTaskNum = " + mTaskNum + ";info = " + info);
                                    mTaskNum++;
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_UPLOAD_LOCAL_DATE_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {

                                mMessageArray.get(mTaskNum).taskId = mTaskNum;
                                UploadMsgEntity info = mMessageArray.get(mTaskNum);
                                if (mView != null && info != null) {
                                    info.errorMsg = message;
                                    info.isEror = true;
                                    mView.uploadCollectDataFail(mMessageArray.get(mTaskNum));
                                    mTaskNum++;
                                    L.e("onError mTaskNum = " + mTaskNum + ";info = " + info);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                UploadMsgEntity info = mMessageArray.get(mTaskNum);
                                if (mView != null && info != null) {
                                    info.errorMsg = message;
                                    info.isEror = true;
                                    mView.uploadCollectDataFail(mMessageArray.get(mTaskNum));
                                    L.e("onError mTaskNum = " + mTaskNum + ";info = " + info);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.uploadCollectDataComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void resetStateAfterUpload() {
        mRefDatas.clear();
        mMessageArray.clear();
        mExtraTransMap.clear();
        mTaskNum = 0;
        info = null;
    }

    /**
     * 计算需要上传的总数
     *
     * @param list
     * @return
     */
    protected List<ReferenceEntity> calcTotalNum(List<ReferenceEntity> list) {
        int size = list.size();
        for (ReferenceEntity referenceEntity : list) {
            referenceEntity.totalCount = size;
        }
        return list;
    }

    public ArrayList<ResultEntity> insertImgNames(ArrayList<ResultEntity> resultEntities){
        if (BuildConfig.APP_NAME.equals("cqzt")) {
            for (int i = 0; i < resultEntities.size(); i++) {
                ResultEntity data = resultEntities.get(i);
                if (!TextUtils.isEmpty(data.businessType) && data.businessType.equals("11")) {
                    ArrayList<ImageEntity> images = mRepository.readImagesByRefNum(data.refCode, true);
                    String imgNames = "";
                    if (images != null && images.size() > 0) {
                        //计算出和当前行匹配的图片数量总和
                        int imgOfLineCount = 0;
                        for (int k = 0; k < images.size(); k++) {
                            if (!TextUtils.isEmpty(images.get(k).refLineId) && images.get(k).refLineId.equals(data.refLineId)) {
                                imgOfLineCount++;
                            }
                        }
                        int findImgOfLineCount = 0;
                        for (int j = 0; j < images.size(); j++) {
                            if (data.refLineId.equals(images.get(j).refLineId)) {
                                findImgOfLineCount++;
                                if (findImgOfLineCount == imgOfLineCount) {
                                    imgNames += images.get(j).imageName;
                                } else {
                                    imgNames += images.get(j).imageName + ",";
                                }
                            }
                        }
                        data.imageName = imgNames;
                    }
                }
            }
            return resultEntities;
        } else {
            return resultEntities;
        }
    }

    /**
     * 根据不同的业务进行转储
     *
     * @param materialDoc:物料凭证
     * @return
     */
    protected Flowable<String> submitData2SAPInner(final String materialDoc) {

        if (mRefDatas == null && mTaskNum < 0 && mTaskNum >= mRefDatas.size()) {
            return Flowable.just("完成!");
        }

        final ReferenceEntity refData = mRefDatas.get(mTaskNum);
        final String bizType = refData.bizType;
        final String transId = refData.transId;
        final String refType = refData.refType;
        final String voucherDate = refData.voucherDate;
        final String userId = refData.recordCreator;
        final String centerCost = refData.costCenter;
        final String projectNum = refData.projectNum;
        final String refNum = refData.recordNum;
        final String refCodeId = refData.refCodeId;
        final int inspectionType = refData.inspectionType;
        mExtraTransMap.clear();
        mExtraTransMap.put("centerCost", centerCost);
        mExtraTransMap.put("projectNum", projectNum);
        mExtraTransMap.put("inspectionType", inspectionType);
        HashMap<String, String> map = getDescByCode(bizType, refType);
        String transToSapFlag = map.get(TRANSTOSAPFLAG_KEY);
        mMessageArray.get(mTaskNum).materialDoc = materialDoc;

        //如果是验收，那么此时需要上传照片
        if (("00".equals(bizType) || "01".equals(bizType)) && !TextUtils.isEmpty(refNum) && !TextUtils.isEmpty(refCodeId)) {
            return Flowable.just(refNum)
                    .flatMap(num -> {
                        ArrayList<ImageEntity> images = mRepository.readImagesByRefNum(num, true);
                        if (images == null || images.size() == 0) {
                            //如果没有图片
                            return transferCollectionData(bizType, transId, refType, userId, voucherDate, transToSapFlag);
                        }
                        return uploadInspectedImages(images, refCodeId, transId, userId, "01")
                                .zipWith(uploadInspectedImages(images, refCodeId, transId, userId, "02"), (s1, s2) -> s2)
                                .doOnNext(a -> {
                                    if ("02".equals(a)) {
                                        mRepository.deleteInspectionImages(refNum, refCodeId, true);
                                        FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false));
                                    }
                                });
                    }).flatMap(s -> transferCollectionData(bizType, transId, refType, userId, voucherDate, transToSapFlag));
        }

        //如果是川庆101入库
        if (Global.CQZT.equals(BuildConfig.APP_NAME) &&
                "11".equals(bizType) && !TextUtils.isEmpty(refNum) && !TextUtils.isEmpty(refCodeId)) {
            return Flowable.just(refNum)
                    .flatMap(num -> {
                        ArrayList<ImageEntity> images = mRepository.readImagesByRefNum(num, true);
                        if(images != null && images.size() > 0) {
                            return uploadInspectedImages(images, refCodeId, transId, userId, "01")
                                    .doOnNext(a -> {
                                        if ("02".equals(a)) {
                                            mRepository.deleteInspectionImages(refNum, refCodeId, true);
                                            FileUtil.deleteDir(FileUtil.getImageCacheDir(mContext.getApplicationContext(), refNum, false));
                                        }
                                    });
                        }
                        return Flowable.just("该单据上传成功!!!");
                    });
        }

        if (TextUtils.isEmpty(transToSapFlag)) {
            //表示该业务不需要转储
            return mRepository.setTransFlag(bizType, transId, "3").flatMap(a -> Flowable.just("完成!"));
        }

        //如果需要转储（比如05）
        return transferCollectionData(bizType, transId, refType, userId, voucherDate, transToSapFlag);
    }


    //数据上传第二步（05）
    private Flowable<String> transferCollectionData(String bizType, String transId, String refType,
                                                    String userId, String voucherDate, String transToSapFlag) {

        return mRepository.setTransFlag(bizType, transId, "3").flatMap(a -> Flowable.just("完成!"))
                .zipWith(mRepository.transferCollectionData(transId, bizType, refType,
                        userId, voucherDate, transToSapFlag, mExtraTransMap)
                        .onErrorResumeNext(new Function<Throwable, Publisher<? extends String>>() {
                            @Override
                            public Publisher<? extends String> apply(Throwable throwable) throws Exception {
                                return mRepository.setTransFlag(bizType, transId, "2")
                                        .flatMap(s -> Flowable.error(throwable));
                            }
                        }), (s, s2) -> s2);
    }


    private Flowable<String> uploadInspectedImages(List<ImageEntity> images, String refCodeId, String transId,
                                                   String userId, String transFileToServer) {
        return Flowable.just(images)
                .flatMap(imgs -> Flowable.fromIterable(wrapperImage(imgs, refCodeId, transId, userId, transFileToServer)))
                .buffer(3)
                .flatMap(results -> mRepository.uploadMultiFiles(results));
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

    /**
     * 图片上传。将imageEntity装换成上传ResultEntity实体类
     *
     * @return
     */
    private ResultEntity wrapperImageInternal(ImageEntity image, String refCodeId, String transId,
                                              String userId, String transFileToServer) {
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

    /**
     * 将单据数据转换为Results
     *
     * @param refData
     * @return
     */
    protected ArrayList<ResultEntity> wrapper2Results(final ReferenceEntity refData, boolean isSave) {
        if (mRefDatas == null) {
            mRefDatas = new ArrayList<>();
        }
        final HashMap<String, String> map = getDescByCode(refData.bizType, refData.refType);
        if (isSave) {
            mRefDatas.add(refData);
            //记录所有的抬头信息
            info = new UploadMsgEntity();
            info.bizType = refData.bizType;
            info.refType = refData.refType;
            info.transId = refData.transId;
            info.refNum = refData.recordNum;
            info.refCodeId = refData.refCodeId;
            info.workId = refData.workId;
            info.invId = refData.invId;
            info.recWorkId = refData.recWorkId;
            info.recInvId = refData.recInvId;
            info.bizTypeDesc = map.get(BIZTYPE_DESC_KEY);
            info.refTypeDesc = map.get(REFTYPE_DESC_KEY);
            info.totalTaskNum = refData.totalCount;
            mMessageArray.put(mTotalUploadDataNum++, info);
        }

        ArrayList<ResultEntity> results = new ArrayList<>();
        final List<RefDetailEntity> details = refData.billDetailList;
        ResultEntity result = null;
        RefDetailEntity item = null;
        for (int i = 0, size = details.size(); i < size; i++) {
            item = details.get(i);
            result = new ResultEntity();
            result.transId = refData.transId;
            result.voucherDate = refData.voucherDate;
            result.refCodeId = refData.refCodeId;
            result.refCode = refData.recordNum;
            result.businessType = refData.bizType;
            result.refType = refData.refType;
            result.moveType = refData.moveType;
            result.invType = refData.invType;
            result.supplierId = refData.supplierId;
            result.supplierNum = refData.supplierNum;
            result.userId = refData.recordCreator;
            result.insId = refData.transId;
            result.inspectionType = refData.inspectionType;
            result.createdBy = refData.recordCreator;
            result.creationDate = refData.creationDate;
            result.lastUpdatedBy = refData.lastUpdatedBy;
            result.lastUpdateDate = refData.lastUpdateDate;
            result.glf = refData.glf;
            result.lyf = refData.lyf;
            result.ckf = refData.ckf;
            result.yfhj = refData.yfhj;
            //明细
            result.transLineId = item.transLineId;
            result.insLineId = item.transLineId;
            result.locationId = item.locationId;
            result.transLineSplitId = item.transLineSplitId;
            result.refLineId = item.refLineId;
            result.refLineNum = item.lineNum;
            result.workId = item.workId;
            result.invId = item.invId;
            result.recWorkId = item.recWorkId;
            result.recInvId = item.recInvId;
            result.materialId = item.materialId;
            result.refDoc = item.refDoc;
            result.refDocItem = item.refDocItem;
            result.insLot = item.insLot;
            result.returnQuantity = item.returnQuantity;
            result.moveCause = item.moveCause;
            result.moveCauseDesc = item.moveCauseDesc;
            result.decisionCode = item.decisionCode;
            result.projectText = item.projectText;
            result.quantity = item.quantity;
            result.quantityCustom = item.quantityCustom;
            result.recQuantity = item.recQuantity;
            result.location = CommonUtil.toUpperCase(item.location);
            result.recLocation = CommonUtil.toUpperCase(item.recLocation);
            result.locationType = item.locationType;
            result.batchFlag = CommonUtil.toUpperCase(item.batchFlag);
            result.recBatchFlag = CommonUtil.toUpperCase(item.recBatchFlag);
            result.specialConvert = item.specialConvert;
            result.materialNum = item.materialNum;
            result.materialDesc = item.materialDesc;
            result.materialGroup = item.materialGroup;
            result.workCode = item.workCode;
            result.invCode = item.invCode;
            //验收数据
            //制造商
            result.manufacturer = item.manufacturer;
            //实收数量
            result.quantity = item.quantity;
            //抽检数量
            result.randomQuantity = item.randomQuantity;
            //完好数量
            result.qualifiedQuantity = item.qualifiedQuantity;
            //损坏数量
            result.damagedQuantity = item.damagedQuantity;
            //送检数量
            result.inspectionQuantity = item.inspectionQuantity;
            //锈蚀数量
            result.rustQuantity = item.rustQuantity;
            //变质
            result.badQuantity = item.badQuantity;
            //其他数量
            result.otherQuantity = item.otherQuantity;
            //包装情况
            result.sapPackage = item.sapPackage;
            //质检单号
            result.qmNum = item.qmNum;
            //索赔单号
            result.claimNum = item.claimNum;
            //合格证
            result.certificate = item.certificate;
            //说明书
            result.instructions = item.instructions;
            //质检证书
            result.qmCertificate = item.qmCertificate;
            //检验结果
            result.inspectionResult = item.inspectionResult;
            result.remark = item.remark;

            result.modifyFlag = "N";

            result.businessTypeDesc = map.get(BIZTYPE_DESC_KEY);
            result.refTypeDesc = map.get(REFTYPE_DESC_KEY);
            results.add(result);
        }
        return results;
    }

    protected HashMap<String, String> getDescByCode(String businessType, String refType) {
        HashMap<String, String> map = new HashMap<>();
        String businessTypeDesc = null;
        String refTypeDesc = null;
        String transToSapFlag = null;
        switch (businessType) {
            case "C01":
                businessTypeDesc = "明盘-无参考";
                transToSapFlag = "05";
                break;
            case "C02":
                businessTypeDesc = "盲盘-无参考";
                transToSapFlag = "";
                break;
            case "00":
            case "01":
                businessTypeDesc = "外观验收";
                transToSapFlag = "";
                break;
            case "11":// 采购入库-101
                businessTypeDesc = "采购入库-101";
                transToSapFlag = "";
                break;
            case "12":// 采购入库-103
                businessTypeDesc = "采购入库-103";
                break;
            case "13":// 采购入库-105(非必检)
                businessTypeDesc = "采购入库-105(非必检)";
                transToSapFlag = "05";
                break;
            case "19":// 委外入库
                break;
            case "19_ZJ":// 委外入库-组件
                break;
            case "110":// 采购入库-105(青海必检)
                businessTypeDesc = "采购入库-105(必检)";
                transToSapFlag = "Z03";
                break;
            case "21":// 销售出库
                break;
            case "23":// 委外发料
                break;
            case "24":// 其他出库-有参考
                break;
            case "38":// UB 351
                businessTypeDesc = "351转储发出-有参考";
                transToSapFlag = "05";
                break;
            case "311":// UB 101
                businessTypeDesc = "101转储接收-有参考";
                transToSapFlag = "05";
                break;
            case "113"://入库通知单
                businessTypeDesc = "物资上架";
                refTypeDesc = "入库通知单";
                transToSapFlag = "02";
                break;
            case "114"://出库通知单
                businessTypeDesc = "物资下架";
                refTypeDesc = "出库通知单";
                transToSapFlag = "02";
                break;
            case "45":// UB 352
                break;
            case "51":// 采购退货-161
                break;
            case "16":// 其他入库-无参考
                break;
            case "25":// 其他出库-无参考
                break;
            case "26":// 无参考-201
                break;
            case "27":// 无参考-221
                break;
            case "32":// 301(无参考)
                break;
            case "34":// 311(无参考)
                businessTypeDesc = "311移库-无参考";
                transToSapFlag = "05";
                break;
            case "44":// 其他退库-无参考
                break;
            case "46":// 无参考-202
                break;
            case "47":// 无参考-222
                break;
            case "71":// 代管料入库
                break;
            case "72":// 代管料出库
                break;
            case "73":// 代管料退库
                break;
            case "74":// 代管料调拨
                break;
            case "91":// 代管料入库-HRM
                break;
            case "92":// 代管料出库-HRM
                break;
            case "93":// 代管料退库-HRM
                break;
            case "94":// 代管料调拨-HRM
                break;
            case "116":
                businessTypeDesc = "调拨入库";
                refTypeDesc = "物料凭证";
                break;

            case "212":
                businessTypeDesc = "其他有参考出库";
                refTypeDesc = "领料申请单";
                break;
            case "314":
                businessTypeDesc = "311移库";
                refTypeDesc = "311-领料申请单";
                break;
            case "317":
                businessTypeDesc = "313移库";
                refTypeDesc = "313-领料申请单";
                break;
            case "310":
                businessTypeDesc = "315移库";
                refTypeDesc = "315-领料申请单";
                break;
            case "411":
                businessTypeDesc = "物资退库";
                break;
        }

       /*   （0：采购订单，1：验收清单，2：生产订单，3：到货验收单，4：交货单，5：出库单，6：提料单，-->
         7：领料申请单，8：批料单，9：SAP出入库单，10：预留单，11 调拨单,12.退库申请单,13.条码出入库单
        14.报废申请单,15.工单,16.入库通知单,17.出库通知单*/

        if (!TextUtils.isEmpty(refType)) {
            List<String> refTypeList = getStringArray(R.array.ref_type_list);
            refTypeDesc = refTypeList.get(Integer.parseInt(refType));
        }
        map.put(BIZTYPE_DESC_KEY, businessTypeDesc);
        map.put(REFTYPE_DESC_KEY, refTypeDesc);
        map.put(TRANSTOSAPFLAG_KEY, transToSapFlag);
        return map;
    }
}
