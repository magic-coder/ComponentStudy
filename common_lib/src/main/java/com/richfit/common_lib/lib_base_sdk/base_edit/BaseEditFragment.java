package com.richfit.common_lib.lib_base_sdk.base_edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by monday on 2017/4/19.
 */

public abstract class BaseEditFragment<P extends IBaseEditPresenter> extends BaseFragment<P>
        implements IBaseEditView {

    AlertDialog.Builder mDialog;

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        isOpenLocationType = Global.OPEN_LOCATION_TYPE.equals(Global.WMFLAG);
        isOpenRecLocationType = Global.OPEN_LOCATION_TYPE.equals(Global.WMFLAG);
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(mActivity);
            mDialog.setTitle("提示");
            mDialog.setMessage("您真的需要修改数据吗?点击确定将完成修改.");
            mDialog.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            mDialog.setPositiveButton("确定", (dialog, which) -> {
                dialog.dismiss();
                saveCollectedData();
            });
        }
        mDialog.show();
    }

    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        ResultEntity result = provideResult();
        if (result == null) {
            showMessage("未获取到条件的数据");
            return;
        }
        mPresenter.uploadCollectionDataSingle(result);
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        showMessage(message);
        mActivity.finish();
    }

    @Override
    public void saveEditedDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {

    }


    @Override
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
    }

    @Override
    public void onDestroy() {
        mDialog = null;
        super.onDestroy();
    }
}
