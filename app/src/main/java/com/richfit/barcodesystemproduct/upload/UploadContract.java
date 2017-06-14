package com.richfit.barcodesystemproduct.upload;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;
import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.UploadMsgEntity;

import java.util.ArrayList;

/**
 * Created by monday on 2017/4/17.
 */

public interface UploadContract {
    interface View extends IBaseDetailView<ResultEntity> {
        void startUploadData(int totalUploadDataNum);

        void uploadCollectDataSuccess(UploadMsgEntity uploadInfo);

        void uploadCollectDataFail(UploadMsgEntity uploadInfo);

        void uploadCollectDataComplete();

        void startReadUploadData();

        void showUploadData(ArrayList<ResultEntity> results);

        void readUploadDataFail(String message);

        void readUploadDataComplete();

    }

    interface Presenter extends IBaseDetailPresenter<View> {
        void uploadCollectedDataOffLine();

        void readUploadData();

        void resetStateAfterUpload();
    }
}
