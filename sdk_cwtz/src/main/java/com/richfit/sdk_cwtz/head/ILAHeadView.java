package com.richfit.sdk_cwtz.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/2/7.
 */

public interface ILAHeadView extends IBaseHeadView {

    void showWorks(List<WorkEntity> works);
    void loadWorksFail(String message);

    void showInvs(List<InvEntity> invs);
    void loadInvsFail(String message);
    void loadInvsComplete();


    void showProjectNums(Map<String,List<SimpleEntity>> map);
    void loadProjectNumsFail(String message);

    void getStorageNumSuccess(String storageNum);
    void getStorageNumFail(String message);
    void clearAllUI();

}
