package com.richfit.module_qysh.module_pd.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/11/28.
 */

public interface IQYSHCNHeadView extends IBaseHeadView {
    void showWorks(List<WorkEntity> works);

    void loadWorksFail(String message);

    void showInvs(List<InvEntity> invs);

    void loadWorkSComplete();

    void loadInvsFail(String message);

    void loadInvsComplete();

    void showStorageNums(List<String> storageNums);
    void loadStorageNumFail(String message);
    void loadStorageNumComplete();

    /**
     * 删除缓存成功
     */
    void deleteCacheSuccess();

    /**
     * 删除缓存失败
     *
     * @param message
     */
    void deleteCacheFail(String message);

    /**
     * 为公共控件绑定数据
     */
    void bindCommonHeaderUI();

    /**
     * 读取单据数据
     *
     * @param refData:单据数据
     */
    void getCheckInfoSuccess(ReferenceEntity refData);

    /**
     * 读取单据数据失败
     *
     * @param message
     */
    void getCheckInfoFail(String message);

    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);
}
