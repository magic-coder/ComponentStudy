package com.richfit.sdk_wzyk.base_msn_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.WorkEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 无参考移库抬头view标准
 * Created by monday on 2016/11/20.
 */

public interface IMSNHeadView extends IBaseHeadView {
    void showWorks(List<WorkEntity> works);
    void loadWorksFail(String message);
    void loadWorksComplete();

    void showSendInvs(List<InvEntity> sendInvs);
    void loadSendInvsFail(String message);
    void loadSendInvsComplete();

    void showRecInvs(List<InvEntity> recInvs);
    void loadRecInvsFail(String message);
    void loadRecInvsComplete();

    void deleteCacheSuccess(String message);
    void deleteCacheFail(String message);

    void showProjectNums(ArrayList<String> projectNums);

    void loadProjectNumsFail(String message);

    /**
     * 数据上传成功后，清除抬头控件的信息
     */
    void clearAllUI();
}
